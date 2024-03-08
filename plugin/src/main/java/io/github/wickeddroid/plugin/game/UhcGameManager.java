package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardEndGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.thread.GameThread;
import io.github.wickeddroid.plugin.thread.ScatterThread;
import io.github.wickeddroid.plugin.world.scatter.ScatterTask;
import io.github.wickeddroid.plugin.world.Worlds;
import io.github.wickeddroid.plugin.world.scatter.adapters.EndAdapter;
import io.github.wickeddroid.plugin.world.scatter.adapters.NetherAdapter;
import io.github.wickeddroid.plugin.world.scatter.adapters.OverworldAdapter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.InjectIgnore;
import team.unnamed.inject.Singleton;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@InjectAll
@Singleton
public class UhcGameManager {

  private Plugin plugin;
  private Worlds worlds;
  private UhcGame uhcGame;
  private Messages messages;
  private GameThread gameThread;
  private MessageHandler messageHandler;
  private ScoreboardGame scoreboardGame;
  private ScoreboardLobby scoreboardLobby;
  private ScoreboardEndGame scoreboardEndGame;
  private UhcTeamRegistry uhcTeamRegistry;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private Game game;
  private Backup backup;
  private UhcEventManager uhcEventManager;
  private OverworldAdapter overworldAdapter;
  private NetherAdapter netherAdapter;
  private EndAdapter endAdapter;
  @InjectIgnore
  private BukkitTask gameTask;

  private CompletableFuture<List<Location>> requestLocations(int count, World world) throws Exception {
    List<Location> locs = new ArrayList<>();
    var env = world.getEnvironment();
    var adapter = env == World.Environment.THE_END ? endAdapter : env == World.Environment.NETHER ? netherAdapter : overworldAdapter;
    CompletableFuture<List<Location>> complFuture = new CompletableFuture<>();

    var future = ScatterTask.scatterTask(world.getName(),  uhcGame.getWorldBorder(), uhcGame.getWorldBorder(), count, worlds.scatter().preventLiquidSpawn(), worlds.scatter().aboveSeaLevel(), worlds.scatter().bannedBiomes(), adapter, progress -> {
      double percentage = (100D/count)*progress;

      var message = messageHandler.parse(messages.other().scatterProgress(), String.valueOf(progress), String.valueOf(count), new DecimalFormat("0.00").format(percentage));

      Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(message));
    });

    future.whenComplete((locations, throwable) -> {
      locs.addAll(locations);
      complFuture.complete(locs);
    });


    return complFuture;
  }

  public void startBackup() {
    for (final var world : Bukkit.getWorlds()) {
      if (this.worlds.blacklist().contains(world.getName())) {
        continue;
      }

      world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
      world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
      world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
      world.getWorldBorder().setDamageAmount(worlds.border().initialBorderDamage());
    }


    gameTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.gameThread, 0L, 20L);
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
      try {
        backup.save();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }, 200L, 12000L);

    UhcEventManager.fireGameStart();
  }

  private void teleportPlayer(List<Location> locations, Player player, boolean laterScatter) {
    var location = locations.stream().findAny().get();
    Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(player, location, laterScatter), 5L);

    if(laterScatter && !game.laterScatterIronman()) { return; }

    uhcGame.getIronmans().add(player.getName());
  }

  public void teleportPlayers(List<Location> locations, boolean tp) {
      uhcGame.getIronmans().clear();
      int delayTeam = 0;

      if (!this.uhcGame.isTeamEnabled() && tp) {
        for (final var uhcPlayer : uhcPlayerRegistry.getPlayers().stream().filter(uP -> !uP.isScattered()).toList()) {
          var location = locations.stream().findAny().get();
          var player = Bukkit.getPlayer(uhcPlayer.getUuid());
          if(player == null) { continue; }

          Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(player, location, false), delayTeam);
          uhcGame.getIronmans().add(player.getName());

          delayTeam += 40;
          locations.remove(location);
        }
      } else if(tp) {
        for (final var team : this.uhcTeamRegistry.getTeams().stream().filter(uT -> !uT.isScattered()).toList()) {
          var location = locations.stream().findAny().get();
          Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(team, location, false), delayTeam);

          team.getMembers().stream().map(Bukkit::getPlayer).forEach(p -> uhcGame.getIronmans().add(p.getName()));
          team.setScattered(true);

          delayTeam += 40;
          locations.remove(location);
        }
      }

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      for (final var player : Bukkit.getOnlinePlayers()) {
        if (this.scoreboardLobby.getSidebar().getViewers().contains(player.getUniqueId())) {
          this.scoreboardLobby.getSidebar().removeViewer(player);
        }

        this.scoreboardGame.getSidebar().addViewer(player);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        if(game.starterInvulnerability()) {
          player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, game.invulnerabilityDuration()*20, 10, false, false, false));
        }

        if(game.initialBoat()) {
          player.getInventory().addItem(new ItemStack(Material.ACACIA_BOAT));
        }
      }

      for (final var world : Bukkit.getWorlds()) {
        if (this.worlds.blacklist().contains(world.getName())) {
          continue;
        }

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        world.getWorldBorder().setDamageAmount(worlds.border().initialBorderDamage());
      }

      this.uhcGame.setGameStart(true);
      this.uhcGame.setUhcGameState(UhcGameState.PLAYING);
      this.uhcGame.setStartTime(System.currentTimeMillis());


      gameTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.gameThread, 0L, 20L);
      Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
        try {
          backup.save();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }, 200L, 12000L);
      Bukkit.getPluginManager().callEvent(new GameStartEvent());
    }, delayTeam);
  }



  public void startGame(final Player sender, boolean tp, World world) {
    if (this.uhcGame.isGameStart() || this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
      this.messageHandler.send(sender, this.messages.game().hasStarted());
      return;
    }

    var toTeleport = uhcPlayerRegistry.getPlayers().stream().filter(uP -> !uP.isScattered()).toList().size();

    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
      try {
        if(tp) {
          var future = requestLocations(uhcGame.isTeamEnabled() ? uhcTeamRegistry.getTeams().stream().filter(uT -> !uT.isScattered()).toList().size() : toTeleport, world);

          future.whenComplete((locations, throwable) -> teleportPlayers(locations, tp));
        } else {
          teleportPlayers(null, tp);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

  }

  public void scatterPlayer(Player player, boolean laterScatter, World world) throws Exception {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
      try {
        var future = requestLocations(1, world);

        future.whenComplete(((locations, throwable) -> {
          teleportPlayer(locations, player, laterScatter);
        }));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }


  public void startMeetup() {
    final var worldsBorder = this.worlds.border().worldBorderWorlds().stream().map(Bukkit::getWorld).toList();

    worldsBorder.forEach(world -> {
      if (world == null) {
        return;
      }

      final var worldBorder = world.getWorldBorder();

      this.uhcGame.setUhcGameState(UhcGameState.MEETUP);

      Bukkit.getScheduler().runTask(plugin, () -> {
        worldBorder.setSize(worlds.border().meetupWorldBorder(), worlds.border().worldBorderDelay());
        worldBorder.setDamageAmount(worlds.border().meetupBorderDamage());
      });

      if(!worlds.border().keepClosingAfterMeetup()) { return; }

      Bukkit.getScheduler().runTaskLater(plugin, ()-> worldBorder.setSize(20, worlds.border().worldBorderDelayAfterMeetup()), worlds.border().worldBorderDelay()*20L);

    });
  }

  public void endGame() {
    this.uhcGame.setUhcGameState(UhcGameState.FINISH);
    UhcEventManager.fireGameEnd();
    Bukkit.getScheduler().cancelTask(this.gameTask.getTaskId());

    Bukkit.getOnlinePlayers().forEach(p -> {
      if(scoreboardGame.getSidebar().getViewers().contains(p.getUniqueId())) {
        scoreboardGame.getSidebar().removeViewer(p);
      }

      scoreboardEndGame.getSidebar().addViewer(p);
    });
  }
}
