package io.github.wickeddroid.plugin.listener.vanilla;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAdvancementCriterionGrantListener implements Listener {

    @EventHandler
    public void onGrant(PlayerAdvancementCriterionGrantEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
    }
}
