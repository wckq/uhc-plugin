package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.api.util.glowing.GlowingEntities;
import io.github.wickeddroid.plugin.UhcPlugin;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Named;

import java.io.IOException;

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
  }

  @Override
  public void unload() {
    this.worldLoader.unload();
    this.glowingEntities.disable();
  }
}
