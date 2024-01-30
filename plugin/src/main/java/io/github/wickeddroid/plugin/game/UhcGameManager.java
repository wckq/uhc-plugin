package io.github.wickeddroid.plugin.game;

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
import io.github.wickeddroid.plugin.util.LocationUtil;
import io.github.wickeddroid.plugin.world.ScatterTask;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
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
  @InjectIgnore
  private BukkitTask gameTask;

  private CompletableFuture<List<Location>> requestLocations(int count, boolean experimental) throws Exception {
    List<Location> locs = new ArrayList<>();

    CompletableFuture<List<Location>> complFuture = new CompletableFuture<>();

    if(experimental) {
      // EXPERIMENTAL - Busca Safe Positions (Eliminando los spawn en Liquid Blocks)
      var future = ScatterTask.scatterTask(this.worlds.worldName(),  uhcGame.getWorldBorder(), uhcGame.getWorldBorder(), count, progress -> {
        double percentage = (100D/count)*progress;

        var message = messageHandler.parse(messages.other().scatterProgress(), String.valueOf(progress), String.valueOf(count), new DecimalFormat("0.00").format(percentage));

        Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(message));
      });

      future.whenComplete((locations1, throwable) -> {
        locs.addAll(locations1);
        complFuture.complete(locs);
      });

      return complFuture;
    } else {
      // TP TRADICIONAL
      while (locs.size() < count) {
        locs.add(LocationUtil.generateRandomLocation(this.uhcGame, this.worlds.worldName()));
      }

      complFuture.complete(locs);

      return complFuture;
    }
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

    Bukkit.getPluginManager().callEvent(new GameStartEvent());
  }

  public void teleportPlayers(List<Location> locations, boolean tp) {
      int delayTeam = 0;

      if (!this.uhcGame.isTeamEnabled() && tp) {
        for (final var uhcPlayer : uhcPlayerRegistry.getPlayers().stream().filter(uP -> !uP.isScattered()).toList()) {
          var location = locations.stream().findAny().get();
          var player = Bukkit.getPlayer(uhcPlayer.getUuid());
          if(player == null) { continue; }

          Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(player, location), delayTeam);
          uhcGame.getIronmans().add(player.getName());

          delayTeam += 40;
          locations.remove(location);
        }
      } else if(tp) {
        for (final var team : this.uhcTeamRegistry.getTeams().stream().filter(uT -> !uT.isScattered()).toList()) {
          var location = locations.stream().findAny().get();
          Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(team, location), delayTeam);

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



  public void startGame(final Player sender, boolean tp) {
    if (this.uhcGame.isGameStart() || this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
      this.messageHandler.send(sender, this.messages.game().hasStarted());
      return;
    }

    var toTeleport = uhcPlayerRegistry.getPlayers().stream().filter(uP -> !uP.isScattered()).toList().size();

    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
      try {
        var future = requestLocations(uhcGame.isTeamEnabled() ? uhcTeamRegistry.getTeams().stream().filter(uT -> !uT.isScattered()).toList().size() : toTeleport, game.useExperimentalScatter());

        future.whenComplete((locations, throwable) -> teleportPlayers(locations, tp));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

  }

  public void startMeetup() {
    final var world = Bukkit.getWorld(this.worlds.worldName());

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
  }

  public void endGame() {
    this.uhcGame.setUhcGameState(UhcGameState.FINISH);
    Bukkit.getScheduler().cancelTask(this.gameTask.getTaskId());

    Bukkit.getOnlinePlayers().forEach(p -> {
      if(scoreboardGame.getSidebar().getViewers().contains(p.getUniqueId())) {
        scoreboardGame.getSidebar().removeViewer(p);
      }

      scoreboardEndGame.getSidebar().addViewer(p);
    });
  }
}
