package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.game.Game;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import team.unnamed.inject.Inject;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class LootGenerateListener implements Listener {

    @Inject
    private Game game;

    @EventHandler
    public void onLoot(LootGenerateEvent event) {
        var ancientNerf = game.ancientCityNerf();

        if(!LootTables.ANCIENT_CITY.getKey().getKey().equals(event.getLootTable().getKey().getKey())) {
            return;
        }

        if(!ancientNerf.enabled()) { return; }

        Map<Integer, ItemStack> loot = new HashMap<>();

        AtomicInteger n = new AtomicInteger(0);

        event.getLoot().forEach(i -> {
            loot.put(n.get(), i);
            n.getAndIncrement();
        });

        loot.forEach((integer, stack) -> {
            if(stack.getType() == Material.ENCHANTED_GOLDEN_APPLE) {
                if(!ancientNerf.enchantedGoldenAppleEnabled()) {
                    event.getLoot().set(integer, null);
                    return;
                }

                var keep = keepItem(ancientNerf.enchantedGoldenAppleChance());

                if(!keep) {
                    if(!ancientNerf.replaceItemOnChange()) {
                        event.getLoot().set(integer, null);
                        return;
                    }

                    event.getLoot().set(integer, randomItem(loot.values()));
                }
            }

            if(stack.getType() == Material.POTION) {
                if(!ancientNerf.regenerationPotionEnabled()) {
                    event.getLoot().set(integer, null);
                    return;
                }

                var keep = keepItem(ancientNerf.regenerationPotionChance());

                if(!keep) {
                    if(!ancientNerf.replaceItemOnChange()) {
                        event.getLoot().set(integer, null);
                        return;
                    }

                    event.getLoot().set(integer, randomItem(loot.values()));
                }
            }

            if(stack.getType().name().toLowerCase().contains("diamond")) {
                if(!ancientNerf.diamondArmorEnabled()) {
                    event.getLoot().set(integer, null);
                    return;
                }

                var keep = keepItem(ancientNerf.diamondArmorChance());

                if(!keep) {
                    if(!ancientNerf.replaceItemOnChange()) {
                        event.getLoot().set(integer, new ItemStack(Material.AIR));
                        return;
                    }
                }

                event.getLoot().set(integer, randomItem(loot.values()));
            }
        });
    }

    private ItemStack randomItem(Collection<ItemStack> itemStackList) {
        var newList = itemStackList.stream().filter(i -> i.getType() != Material.ENCHANTED_GOLDEN_APPLE && i.getType() != Material.POTION && !i.getType().name().toLowerCase().contains("diamond")).toList();

        var item = newList.get(ThreadLocalRandom.current().nextInt(newList.size()));;
        var itemstack = new ItemStack(item.getType(), item.getAmount() <= 1 ? 1 : ThreadLocalRandom.current().nextInt(1, item.getAmount()));
        itemstack.setItemMeta(item.getItemMeta());
        return itemstack;
    }

    private boolean keepItem(int n) {
        if(n <= 0) { return false; }
        if(n >= 100) { return true; }
        return ThreadLocalRandom.current().nextInt(0, 100) < n;
    }
}
