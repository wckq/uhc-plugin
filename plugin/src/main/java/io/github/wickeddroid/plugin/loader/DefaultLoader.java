package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.api.util.glowing.GlowingEntities;
import io.github.wickeddroid.plugin.UhcPlugin;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.SaveLoad;
import io.github.wickeddroid.plugin.world.ScatterTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Named;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@InjectAll
public class DefaultLoader implements Loader {

  @Named("command-loader")
  private Loader commandLoader;

  @Named("listener-loader")
  private Loader listenerLoader;

  @Named("world-loader")
  private Loader worldLoader;

  @Named("scenario-loader")
  private Loader scenarioLoader;

  private UhcGame uhcGame;
  private UhcTeamManager uhcTeamManager;
  private UhcTeamRegistry uhcTeamRegistry;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private UhcPlugin plugin;
  private Backup backup;
  private GlowingEntities glowingEntities;

  @Override
  public void load() {
    this.worldLoader.load();
    this.commandLoader.load();
    this.listenerLoader.load();
    this.scenarioLoader.load();

    Bukkit.getServer().setDefaultGameMode(GameMode.ADVENTURE);

    try {
      backup.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
      try {
        var task = ScatterTask.scatterTask(Bukkit.getWorlds().get(2).getName(), 2000, 2000, 75, true, true, Collections.emptyList(), i ->{});

        task.whenComplete((locations, throwable) -> {
          locations.forEach(location -> {
            Bukkit.getLogger().info(String.format("Y = %s | Biome = %s | Block = %s",
                    Math.round(location.getY()),
                    location.getBlock().getBiome(),
                    location.getBlock().getType()
                    ));
          });
        });
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public void unload() {
    this.worldLoader.unload();
    this.glowingEntities.disable();
  }
}
