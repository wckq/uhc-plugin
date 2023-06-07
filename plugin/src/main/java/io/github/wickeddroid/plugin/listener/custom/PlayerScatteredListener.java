package io.github.wickeddroid.plugin.listener.custom;

import io.github.wickeddroid.plugin.event.game.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;

@InjectAll
public class PlayerScatteredListener implements Listener {

  private Messages messages;
  private MessageHandler messageHandler;
  private UhcPlayerRegistry uhcPlayerRegistry;

  @EventHandler
  public void onPlayerScattered(final PlayerScatteredEvent event) {
    final var playerScattered = event.getPlayer();
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerScattered.getName());

    if (uhcPlayer == null) {
      return;
    }

    playerScattered.setGameMode(GameMode.SURVIVAL);
    playerScattered.getInventory().clear();
    playerScattered.setFoodLevel(20);
    playerScattered.setHealth(20);
    playerScattered.setLevel(0);
    playerScattered.setExp(0);

    playerScattered.addPotionEffect(PotionEffectType.SLOW.createEffect(Integer.MAX_VALUE, 255));
    playerScattered.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(Integer.MAX_VALUE, 255));
    playerScattered.addPotionEffect(PotionEffectType.JUMP.createEffect(Integer.MAX_VALUE, 127));

    playerScattered.teleport(event.getLocation());

    uhcPlayer.setScattered(true);

    for (final var player : Bukkit.getOnlinePlayers()) {
      this.messageHandler.send(player, this.messages.other().scattered(), playerScattered.getName());
    }
  }
}
