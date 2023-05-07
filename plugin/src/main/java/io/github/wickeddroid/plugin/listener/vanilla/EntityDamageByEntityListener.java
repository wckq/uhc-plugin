package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.team.UhcTeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import team.unnamed.inject.Inject;

public class EntityDamageByEntityListener implements Listener {
  @Inject private UhcTeamManager uhcTeamManager;

  @EventHandler
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    if (!(event.getDamager() instanceof Player damager)) {
      return;
    }

    final var uhcTeam = this.uhcTeamManager.getTeamByPlayer(player.getName());

    if (uhcTeam == null) {
      return;
    }

    if (!uhcTeam.getMembers().contains(damager.getName())) {
      return;
    }

    event.setCancelled(true);
  }
}
