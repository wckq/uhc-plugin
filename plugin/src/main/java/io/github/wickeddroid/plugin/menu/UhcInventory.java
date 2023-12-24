package io.github.wickeddroid.plugin.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;

public abstract class UhcInventory {

  protected final String title;
  protected final int rows;

  public UhcInventory(
          final String title,
          final int rows
  ) {
    this.title = title;
    this.rows = rows;
  }


  public abstract Inventory createInventory();
}
