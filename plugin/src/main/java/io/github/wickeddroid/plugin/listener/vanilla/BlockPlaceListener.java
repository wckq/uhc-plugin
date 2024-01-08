package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.plugin.util.PluginUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import team.unnamed.inject.Inject;

public class BlockPlaceListener implements Listener {

    @Inject
    private Worlds worlds;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var world = event.getBlock().getWorld();

        if(worlds.blacklist().contains(world.getName()) && !event.getPlayer().hasPermission(PluginUtil.OVERRIDE_LOBBY_PROTECTION_PERMISSION)) {
            event.setCancelled(true);
        }
    }
}
