package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.util.PluginUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

public class BlockBreakListener implements Listener {

  @Inject
  private UhcGame uhcGame;
  @Inject
  private Worlds worlds;

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    var world = event.getBlock().getWorld();

    if(worlds.blacklist().contains(world.getName()) && !event.getPlayer().hasPermission(PluginUtil.OVERRIDE_LOBBY_PROTECTION_PERMISSION)) {
      event.setCancelled(true);
      return;
    }

    if (this.uhcGame.getUhcGameState() == UhcGameState.WAITING && !worlds.blacklist().contains(world.getName())) {
      event.setCancelled(true);
    }
  }
}
