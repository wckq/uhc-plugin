package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardEndGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import team.unnamed.inject.Inject;

public class PlayerJoinListener implements Listener {

  @Inject private UhcPlayerRegistry uhcPlayerRegistry;
  @Inject private ScoreboardEndGame scoreboardEndGame;
  @Inject private ScoreboardLobby scoreboardLobby;
  @Inject private ScoreboardGame scoreboardGame;
  @Inject private UhcGame uhcGame;

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var playerName = player.getName();

    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerName);

    if (uhcPlayer == null) {
      this.uhcPlayerRegistry.createPlayer(player.getUniqueId(), playerName);

    }

    if (this.uhcGame.getUhcGameState() == UhcGameState.WAITING) {
      this.scoreboardLobby.getSidebar().addViewer(player);
    } else if (this.uhcGame.getUhcGameState() == UhcGameState.FINISH) {
      this.scoreboardEndGame.getSidebar().addViewer(player);
    } else {
      this.scoreboardGame.getSidebar().addViewer(player);
    }
  }
}
