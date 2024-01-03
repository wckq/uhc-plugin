package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.N;
import team.unnamed.inject.Inject;

public class EntityDamageListener implements Listener {

  @Inject
  private UhcGame uhcGame;
  @Inject
  private Messages messages;
  @Inject
  private MessageHandler messageHandler;
  @Inject
  private Game game;

  private int lostIronmans = 0;

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    if (this.uhcGame.getUhcGameState() == UhcGameState.WAITING || this.uhcGame.getUhcGameState() == UhcGameState.FINISH) {
      if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
        return;
      }

      event.setCancelled(true);

      return;
    }


    if(event.getEntity() instanceof Player) {
      var player = (Player) event.getEntity();

      var isIronman = uhcGame.getIronmans().contains(player);

      if(!isIronman) { return; }

      var resistance = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

      if (resistance != null && resistance.getAmplifier() >= 4) {
        return;
      }

      if (player.isBlocking() && event.getDamage() != 0) {
        return;
      }

      if(!game.ironmanEnabled()) { return; }

      uhcGame.removeIronman(player);
      lostIronmans++;

      messageHandler.sendGlobal(messages.game().ironmanLost(), player.getName(), String.valueOf(uhcGame.getIronmans().size()));

      if(lostIronmans == 1 && game.papermanEnabled()) {
        messageHandler.sendGlobal(messages.game().papermanPlayer(), player.getName());
      }

      if(uhcGame.getIronmans().size() == 1) {
        messageHandler.sendGlobal(messages.game().ironmanPlayer(), uhcGame.getIronmans().get(0).getName());
      }
    }
  }
}
