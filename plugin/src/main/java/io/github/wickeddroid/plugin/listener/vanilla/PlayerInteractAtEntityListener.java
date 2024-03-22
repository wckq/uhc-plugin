package io.github.wickeddroid.plugin.listener.vanilla;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType() == EntityType.PLAYER) {
            if(event.getPlayer().getGameMode() != GameMode.SPECTATOR) { return; }

            var player = (Player)event.getRightClicked();

            if(player.getGameMode() != GameMode.SURVIVAL) { return; }

            if(!event.getPlayer().hasPermission("uhc.staff.inventory.rightclick")) { return; }

            event.getPlayer().performCommand("inv " + player.getName());
        }

        if(event.getRightClicked().getType() == EntityType.VILLAGER) {
            if(event.getPlayer().getGameMode() != GameMode.SPECTATOR) { return; }

            var player = event.getPlayer();
            var villager = (Villager)event.getRightClicked();

            if(!event.getPlayer().hasPermission("uhc.staff.inventory.villager")) { return; }

            player.openMerchant(villager, true);
        }
    }
}
