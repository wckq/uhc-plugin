package io.github.wickeddroid.plugin.util;

import org.bukkit.inventory.Inventory;

public final class InventoryUtil {
  private InventoryUtil() {}

  public static boolean isCustomInventory(final Inventory inventory) {
    if (inventory == null) {
      return false;
    }

    final var holder = inventory.getHolder();

    return true;
  }
}
