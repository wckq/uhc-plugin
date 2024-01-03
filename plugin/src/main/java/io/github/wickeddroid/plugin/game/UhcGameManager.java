package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.events.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.message.title.Titles;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.thread.GameThread;
import io.github.wickeddroid.plugin.thread.ScatterThread;
import io.github.wickeddroid.plugin.util.LocationUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.InjectAll;

import java.io.IOException;

@InjectAll
public class UhcGameManager {

  private Plugin plugin;
  private Worlds worlds;
  private Titles titles;
  private UhcGame uhcGame;
  private Messages messages;
  private GameThread gameThread;
  private MessageHandler messageHandler;
  private ScoreboardGame scoreboardGame;
  private ScoreboardLobby scoreboardLobby;
  private UhcTeamRegistry uhcTeamRegistry;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private Game game;
  private Backup backup;

  public void startGame(final Player sender, boolean tp) {
    if (this.uhcGame.isGameStart() || this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
      this.messageHandler.send(sender, this.messages.game().hasStarted());
      return;
    }

    var delayTeam = 0;

    if (!this.uhcGame.isTeamEnabled() && tp) {
      for (final var player : Bukkit.getOnlinePlayers()) {
        final var location = LocationUtil.generateRandomLocation(this.uhcGame, this.worlds.worldName());

        if (location == null) {
          continue;
        }

        Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(player, location), delayTeam);
        uhcGame.addIronman(player);

        delayTeam += 40;
      }
    } else {
      if (tp) {
        for (final var team : this.uhcTeamRegistry.getTeams()) {
          final var location = LocationUtil.generateRandomLocation(this.uhcGame, this.worlds.worldName());

          if (location == null) {
            continue;
          }
          Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(team, location), delayTeam);

          team.getMembers().stream().map(Bukkit::getPlayer).forEach(p -> uhcGame.addIronman(p));

          delayTeam += 40;
        }
      }
    }

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      for (final var player : Bukkit.getOnlinePlayers()) {
        if (this.scoreboardLobby.getSidebar().getViewers().contains(player.getUniqueId())) {
          this.scoreboardLobby.getSidebar().removeViewer(player);
        }

        this.scoreboardGame.getSidebar().addViewer(player);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.showTitle(this.titles.gameStart());

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


      Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.gameThread, 0L, 20L);
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

  public void startMeetup() {
    final var world = Bukkit.getWorld(this.worlds.worldName());

    if (world == null) {
      return;
    }

    final var worldBorder = world.getWorldBorder();

    for (final var player : Bukkit.getOnlinePlayers()) {
      player.showTitle(this.titles.meetupTitle());
    }

    this.uhcGame.setUhcGameState(UhcGameState.MEETUP);

    Bukkit.getScheduler().runTask(plugin, () -> {
      worldBorder.setSize(worlds.border().meetupWorldBorder(), worlds.border().worldBorderDelay());
      worldBorder.setDamageAmount(worlds.border().meetupBorderDamage());
    });

    if(!worlds.border().keepClosingAfterMeetup()) { return; }

    Bukkit.getScheduler().runTaskLater(plugin, ()-> worldBorder.setSize(20, worlds.border().worldBorderDelayAfterMeetup()), worlds.border().worldBorderDelay()*20L);
  }
}
