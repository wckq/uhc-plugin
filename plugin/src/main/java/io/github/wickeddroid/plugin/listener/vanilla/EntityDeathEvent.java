package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

import java.util.ArrayList;

public class EntityDeathEvent implements Listener {

    @Inject
    private Game game;

    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if(!game.replaceGhastDrop()) { return; }
        if(event.getEntityType() != EntityType.GHAST) { return; }
        if(event.getDrops().isEmpty()) { return; }

        event.getDrops().clear();
        event.getDrops().add(new ItemStack(Material.GOLD_INGOT));
    }
}
