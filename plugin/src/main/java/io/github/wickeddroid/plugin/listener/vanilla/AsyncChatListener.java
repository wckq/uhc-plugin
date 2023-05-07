package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.inject.Inject;

public class AsyncChatListener implements Listener {
  @Inject private Messages messages;
  @Inject private UhcPlayerRegistry uhcPlayerRegistry;
  @Inject private UhcTeamManager uhcTeamManager;

  @EventHandler
  public void onAsyncChat(final AsyncChatEvent event) {
    final var player = event.getPlayer();
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());

    if (uhcPlayer == null) {
      return;
    }

    final var uhcTeam = uhcPlayer.getUhcTeam();

    if (uhcTeam == null) {
      uhcPlayer.setTeamChat(false);
      return;
    }

    if (!uhcPlayer.isTeamChat()) {
      return;
    }

    this.uhcTeamManager.sendMessageTeam(player, event.message());

    event.setCancelled(true);
  }
}
