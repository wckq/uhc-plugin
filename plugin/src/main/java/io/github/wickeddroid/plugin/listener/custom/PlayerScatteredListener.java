package io.github.wickeddroid.plugin.listener.custom;

import io.github.wickeddroid.plugin.event.game.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.inject.Inject;

public class PlayerScatteredListener implements Listener {
  @Inject private Messages messages;
  @Inject private MessageHandler messageHandler;
  @Inject private UhcPlayerRegistry uhcPlayerRegistry;

  @EventHandler
  public void onPlayerScattered(final PlayerScatteredEvent event) {
    final var playerScattered = event.getPlayer();
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerScattered.getName());

    if (uhcPlayer == null) {
      return;
    }

    playerScattered.setGameMode(GameMode.SURVIVAL);
    playerScattered.setHealth(20);
    playerScattered.setExp(0);
    playerScattered.setLevel(0);
    playerScattered.setFoodLevel(20);
    playerScattered.getInventory().clear();

    playerScattered.teleport(event.getLocation());

    uhcPlayer.setScattered(true);

    for (final var player : Bukkit.getOnlinePlayers()) {
      this.messageHandler.send(player, this.messages.other().scattered(), playerScattered.getName());
    }
  }
}
