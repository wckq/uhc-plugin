package io.github.wickeddroid.plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.World;

public final class WorldUtil {
  private WorldUtil() {}

  public static World getWorld(final String name) {
    return Bukkit.getWorld(name);
  }
}
