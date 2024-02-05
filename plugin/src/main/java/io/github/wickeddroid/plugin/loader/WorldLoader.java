package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.util.PropertiesUtil;
import io.github.wickeddroid.plugin.world.WorldGenerator;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import team.unnamed.inject.Inject;

import java.io.File;
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

    this.worlds.border().worldBorderWorlds().forEach(w -> {
      this.worldGenerator.setupWorldBorder(Bukkit.getWorld(w), this.worlds.border().worldBorder());
    });

    var seed = Bukkit.getWorld(this.worlds.worldName()).getSeed();
    var rootFile = new File(".");
    var propertiesFile = new File(rootFile.getAbsolutePath() + "/server.properties");
    var properties = new PropertiesUtil(propertiesFile);

    var levelSeed = properties.getProperty("level-seed");

    if(levelSeed == null || !levelSeed.equalsIgnoreCase(String.valueOf(seed))) {
      properties.setProperty("level-seed", String.valueOf(seed));
      var defaultWorld = properties.getProperty("level-name");

      try {
        this.worldGenerator.removeWorld(Bukkit.getWorld(defaultWorld+"_nether"), true);
        this.worldGenerator.removeWorld(Bukkit.getWorld(defaultWorld+"_the_end"), true);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }



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
        this.worldGenerator.removeWorld(world, false);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    this.worldGenerator.removeLobbyData();
  }
}
