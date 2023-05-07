package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import team.unnamed.inject.Inject;

public class PlayerJoinListener implements Listener {

  @Inject
  private UhcPlayerRegistry uhcPlayerRegistry;
  @Inject
  private ScoreboardLobby scoreboardLobby;

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var playerName = player.getName();

    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerName);

    if (uhcPlayer == null) {
      this.uhcPlayerRegistry.createPlayer(player.getUniqueId(), playerName);
    }

    this.scoreboardLobby.getSidebar().addViewer(player);
  }
}
