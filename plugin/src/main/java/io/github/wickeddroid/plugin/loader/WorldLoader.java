package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.world.WorldGenerator;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import team.unnamed.inject.Inject;

import java.io.IOException;

public class WorldLoader implements Loader {
  @Inject private Worlds worlds;
  @Inject private WorldGenerator worldGenerator;

  @Override
  public void load() {
    for (final var world : Bukkit.getWorlds()) {
      if (this.worlds.blacklist().contains(world.getName())) {
        continue;
      }

      this.worldGenerator.applySettings(world);
    }

    this.worldGenerator.createWorld(this.worlds.worldName());
  }

  @Override
  public void unload() {
    for (final var world : Bukkit.getWorlds()) {
      if (world == null) {
        continue;
      }

      if (this.worlds.blacklist().contains(world.getName())) {
        continue;
      }

      try {
        this.worldGenerator.removeWorld(world);
      } catch (IOException e) {
        throw new IllegalArgumentException("The world don't exists");
      }
    }
  }
}
