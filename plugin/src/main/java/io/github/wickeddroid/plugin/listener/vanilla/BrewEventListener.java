package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import team.unnamed.inject.Inject;

public class BrewEventListener implements Listener {

    @Inject
    private Game game;
    @EventHandler
    public void onBrew(BrewEvent event) {

        var inventory = event.getContents();

        if(inventory.getType() != InventoryType.BREWING) { return; }

        var ingredientItem = inventory.getItem(3);
        if(ingredientItem == null) { return; }

        var ingredient = ingredientItem.getType();

        if(ingredient == Material.GHAST_TEAR && game.banRegenerationPotion()) {
            event.setCancelled(true);
        }


        if(ingredient == Material.GLOWSTONE_DUST) {
            if(!game.banAmplifiedStrengthPotion()) { return; }

            var pot1 = checkPotion(inventory.getItem(0));
            var pot2 = checkPotion(inventory.getItem(1));
            var pot3 = checkPotion(inventory.getItem(2));

            if(pot1 || pot2 || pot3) {
                event.setCancelled(true);
            }
        }
    }

    private boolean checkPotion(ItemStack potion) {
        if(potion == null) {
            return false;
        }

        var meta = (PotionMeta)potion.getItemMeta();

        return meta.getBasePotionData().getType() == PotionType.STRENGTH;
    }
}