package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.UhcPlugin;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.SaveLoad;
import org.bukkit.Bukkit;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Named;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

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
  private UhcPlugin plugin;

  @Override
  public void load() {
    this.worldLoader.load();
    this.commandLoader.load();
    this.listenerLoader.load();
    this.scenarioLoader.load();

    var file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "team_registry_backup.bin");

    try {
      uhcTeamRegistry.setBackupTeams(SaveLoad.load(file.getAbsolutePath()));
      file.deleteOnExit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void unload() {
    this.worldLoader.unload();

    if(uhcGame.getUhcGameState() == UhcGameState.WAITING || uhcGame.getUhcGameState() == UhcGameState.FINISH) {
      return;
    }

    var teams = uhcTeamRegistry.getTeamMap();

    try {
      SaveLoad.save(teams, plugin.getDataFolder().getAbsolutePath() + File.separator + "team_registry_backup.bin");

      Bukkit.getLogger().severe("Saved backup teams");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
