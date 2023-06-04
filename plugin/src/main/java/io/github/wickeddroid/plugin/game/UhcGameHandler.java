package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.message.title.Titles;
import io.github.wickeddroid.plugin.thread.GameThread;
import io.github.wickeddroid.plugin.thread.ScatterThread;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.LocationUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;

@InjectAll
public class UhcGameHandler {
  private Plugin plugin;
  private Worlds worlds;
  private Titles titles;
  private UhcGame uhcGame;
  private Messages messages;
  private GameThread gameThread;
  private MessageHandler messageHandler;
  private UhcTeamRegistry uhcTeamRegistry;
  private ScoreboardLobby scoreboardLobby;
  private ScoreboardGame scoreboardGame;

  public void startGame(final Player sender) {
    if (this.uhcGame.isGameStart() || this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
      this.messageHandler.send(sender, this.messages.game().hasStarted());
      return;
    }

    final var uhcWorld = Bukkit.getWorld(this.worlds.worldName());

    var delayTeam = 0;

    if (!this.uhcGame.isTeamEnabled()) {
      for (final var player : Bukkit.getOnlinePlayers()) {
        final var location = LocationUtil.generateRandomLocation(this.uhcGame, this.worlds.worldName());

        if (location == null) {
          continue;
        }

        Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(player, location), delayTeam);

        delayTeam += 40;
      }
    } else {
      for (final var team : this.uhcTeamRegistry.getTeams()) {
        final var location = LocationUtil.generateRandomLocation(this.uhcGame, this.worlds.worldName());

        if (location == null) {
          continue;
        }
        Bukkit.getScheduler().runTaskLater(plugin, new ScatterThread(team, location), delayTeam);

        delayTeam += 40;
      }
    }

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      if (uhcWorld == null) {
        return;
      }

      for (final var player : Bukkit.getOnlinePlayers()) {
        if (this.scoreboardLobby.getSidebar().getViewers().contains(player.getUniqueId())) {
          this.scoreboardLobby.getSidebar().removeViewer(player);
        }

        this.scoreboardGame.getSidebar().addViewer(player);

        player.showTitle(this.titles.gameStart());
      }

      uhcWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
      uhcWorld.setGameRule(GameRule.DO_MOB_SPAWNING, true);
      uhcWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, true);

      this.uhcGame.setGameStart(true);
      this.uhcGame.setUhcGameState(UhcGameState.PLAYING);
      this.uhcGame.setStartTime(System.currentTimeMillis());

      Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.gameThread, 0L, 20L);
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

    this.uhcGame.setUhcGameState(UhcGameState.MEETUP  );
    Bukkit.getScheduler().runTask(plugin, () -> worldBorder.setSize(300, 300));
  }

  public void changePvp(final boolean pvp) {
    for (final var world : Bukkit.getWorlds()) {
      if (world == null) {
        continue;
      }

      world.setPVP(pvp);
    }

    if (pvp) {
      for (final var player : Bukkit.getOnlinePlayers()) {
        player.showTitle(this.titles.pvpTitle());
      }
    }

    this.uhcGame.setUhcGameState(UhcGameState.PVP);
    this.uhcGame.setPvp(pvp);
  }
}
