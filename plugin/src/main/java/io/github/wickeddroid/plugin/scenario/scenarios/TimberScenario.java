package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

import java.util.concurrent.ThreadLocalRandom;

@RegisteredScenario
@Scenario(
        name = "Timber",
        key = "timber",
        description = {
                "<gray>- Los árboles se talan automáticamente."
        },
        material = Material.OAK_LOG
)
public class TimberScenario extends ListenerScenario {

    @Inject
    private Plugin plugin;
    @Inject
    private Worlds worlds;
    @Inject
    private UhcGame uhcGame;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();

        if(worlds.blacklist().contains(e.getBlock().getWorld().getName())) { return; }

        if (isLog(block.getType())) {
            breakTree(block);
        }
    }

    private void breakTree(Block block) {
        if (isLog(block.getType())) {
            block.breakNaturally(new ItemStack(Material.AIR), true);
            double radius = radius(block.getType());

            for (var face : BlockFace.values()){
                if(isLog(block.getRelative(face).getType())) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> breakTree(block.getRelative(face)), 3L);
                    continue;
                }

                if(!isLog(block.getRelative(BlockFace.UP).getType())) {
                    Bukkit.getScheduler().runTaskLater(plugin, ()-> breakLeaves(block.getRelative(face), radius, block.getLocation()), 3L);
                }
            }
        }
    }

    private void breakLeaves(Block block, double radius, Location centralLoc) {
        if(isLeaves(block.getType())) {
            if(centralLoc.distance(block.getLocation()) > radius) {
                return;
            }

            block.breakNaturally(new ItemStack(Material.AIR), true);

            if(block.getType().toString().toLowerCase().endsWith("_leaves")) {
                if (ThreadLocalRandom.current().nextInt(0,100) < uhcGame.getAppleRate()) {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
                }
            }

            for(var face : BlockFace.values()) {
                Bukkit.getScheduler().runTaskLater(plugin, ()-> breakLeaves(block.getRelative(face), radius, centralLoc),3L);
            }
        }
    }

    private boolean isLog(Material material) {
        return (material.toString().toLowerCase().endsWith("_log") || material.toString().toLowerCase().endsWith("_stem"));
    }

    private boolean isLeaves(Material material) {
        return (material.toString().toLowerCase().endsWith("_leaves") || material == Material.BROWN_MUSHROOM_BLOCK || material == Material.RED_MUSHROOM_BLOCK || material.toString().toLowerCase().endsWith("_log"));
    }

    private double radius(Material material) {
        var CHERRY_LOG = getNewerMaterial("CHERRY_LOG");
        if(CHERRY_LOG != null) { return 7.0D; }


        switch (material) {
            case JUNGLE_LOG -> {
                return 10.0D;
            }
            case DARK_OAK_LOG, MUSHROOM_STEM, SPRUCE_LOG -> {
                return 6.0D;
            }
            case ACACIA_LOG -> {
                return 7.0D;
            }
            default -> {
                return 5.0D;
            }
        }
    }


    private Material getNewerMaterial(String key) {
        try {
            return Material.valueOf(key);
        } catch (Exception e) {
            return null;
        }
    }
}

