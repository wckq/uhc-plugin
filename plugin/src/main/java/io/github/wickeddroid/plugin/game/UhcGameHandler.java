package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.message.title.Titles;
import io.github.wickeddroid.plugin.runnable.GameRunnable;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

public class UhcGameHandler {
  @Inject private Plugin plugin;
  @Inject private Worlds worlds;
  @Inject private Titles titles;
  @Inject private UhcGame uhcGame;
  @Inject private Messages messages;
  @Inject private GameRunnable gameRunnable;
  @Inject private MessageHandler messageHandler;

  public void startGame(final Player sender) {
    if (this.uhcGame.isGameStart() || this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
      this.messageHandler.send(sender, this.messages.game().hasStarted());
      return;
    }

    this.uhcGame.setGameStart(true);
    this.uhcGame.setUhcGameState(UhcGameState.STARTING);
    this.uhcGame.setStartTime(System.currentTimeMillis());

    this.gameRunnable.runTaskTimerAsynchronously(plugin, 0L, 20L);
  }

  public void startMeetup() {

  }

  public void changePvp(final boolean pvp) {
    for (final var world : Bukkit.getWorlds()) {
      if (world == null) {
        continue;
      }

      if (this.worlds.blacklist().contains(world.getName())) {
        continue;
      }

      world.setPVP(pvp);
    }

    if (pvp) {
      for (final var player : Bukkit.getOnlinePlayers()) {
        player.showTitle(this.titles.pvpTitle());
      }
    }

    this.uhcGame.setPvp(pvp);
  }
}
