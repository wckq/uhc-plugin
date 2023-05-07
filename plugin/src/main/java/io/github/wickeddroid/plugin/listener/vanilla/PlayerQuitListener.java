package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.inject.Inject;

public class PlayerQuitListener implements Listener {

  @Inject
  private UhcPlayerRegistry uhcPlayerRegistry;

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    final var player = event.getPlayer();
    final var playerName = player.getName();
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerName);

    if (uhcPlayer != null) {
      this.uhcPlayerRegistry.removePlayer(playerName);
    }
  }
}
