package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.util.PluginUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

public class LeavesDecayListener implements Listener {

  @Inject
  private UhcGame uhcGame;

  @EventHandler
  public void onLeavesDecay(final LeavesDecayEvent event) {
    final var world = event.getBlock();
    final var appleRate = this.uhcGame.getAppleRate();

    if (PluginUtil.RANDOM.nextInt(0, 100) < appleRate) {
      world.getWorld().dropItemNaturally(world.getLocation(), new ItemStack(Material.APPLE));
    }
  }
}
