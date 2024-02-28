package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.event.player.PlayerIronmanStatusChangeEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.N;
import team.unnamed.inject.Inject;

import java.io.IOException;

public class EntityDamageListener implements Listener {

  @Inject
  private UhcGame uhcGame;
  @Inject
  private Messages messages;
  @Inject
  private MessageHandler messageHandler;
  @Inject
  private Game game;
  @Inject
  private ScenarioManager scenarioManager;
  @Inject
  private Backup backup;
  @Inject
  private Plugin plugin;

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

      Bukkit.getScheduler().runTaskLater(plugin, ()-> {
        if(player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
          if(player.getAbsorptionAmount() <= 0.0D) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
          }
        }

      }, 5L);

      var isIronman = uhcGame.getIronmans().contains(player.getName());

      if(!isIronman) { return; }

      var resistance = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

      if (resistance != null && resistance.getAmplifier() >= 4) {
        return;
      }

      if (player.isBlocking() && event.getDamage() != 0) {
        return;
      }

      if(event.getCause() == EntityDamageEvent.DamageCause.FALL && scenarioManager.isEnabled("no_fall")) {
        return;
      }

      var fireLess = scenarioManager.isEnabled("fire_less");

      if(fireLess) {
        var cause = event.getCause();
        var prevent_damage_fire = scenarioManager.getOption("fire_less", "prevent_damage_fire").getAsBoolean();
        var prevent_damage_lava = scenarioManager.getOption("fire_less", "prevent_damage_lava").getAsBoolean();
        var prevent_damage_burn = scenarioManager.getOption("fire_less", "prevent_damage_burn").getAsBoolean();
        var prevent_damage_magma = scenarioManager.getOption("fire_less", "prevent_damage_magma").getAsBoolean();

        if(
                        (prevent_damage_fire && cause == EntityDamageEvent.DamageCause.FIRE) ||
                        (prevent_damage_lava && cause == EntityDamageEvent.DamageCause.LAVA) ||
                        (prevent_damage_burn && cause == EntityDamageEvent.DamageCause.FIRE_TICK) ||
                        (prevent_damage_magma && cause == EntityDamageEvent.DamageCause.HOT_FLOOR)
        ) {
          return;
        }
      }

      if(!game.ironmanEnabled()) { return; }

      if(uhcGame.getIronmans().size() == 1) { return; }

      uhcGame.getIronmans().remove(player.getName());

      UhcEventManager.firePlayerIronmanStatusChange(player, PlayerIronmanStatusChangeEvent.Status.LOST, PlayerIronmanStatusChangeEvent.Status.COMPETITOR);
      messageHandler.sendGlobal(messages.game().ironmanLost(), player.getName(), String.valueOf(uhcGame.getIronmans().size()));

      if(uhcGame.paperman() == null && game.papermanEnabled()) {
        messageHandler.sendGlobal(messages.game().papermanPlayer(), player.getName());
        UhcEventManager.firePlayerIronmanStatusChange(player, PlayerIronmanStatusChangeEvent.Status.PAPERMAN, PlayerIronmanStatusChangeEvent.Status.LOST);
        uhcGame.setPaperman(player.getName());
      }

      if(uhcGame.getIronmans().size() == 1) {
        messageHandler.sendGlobal(messages.game().ironmanPlayer(), uhcGame.getIronmans().get(0));
        UhcEventManager.firePlayerIronmanStatusChange(player, PlayerIronmanStatusChangeEvent.Status.WINNER, PlayerIronmanStatusChangeEvent.Status.COMPETITOR);
        uhcGame.setIronman(uhcGame.getIronmans().get(0));
      }

      try {
        backup.save();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
