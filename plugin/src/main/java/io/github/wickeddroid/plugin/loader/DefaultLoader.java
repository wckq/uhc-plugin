package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.UhcPlugin;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.SaveLoad;
import org.bukkit.Bukkit;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Named;

import java.io.File;
import java.io.FileNotFoundException;
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

  @Override
  public void load() {
    this.worldLoader.load();
    this.commandLoader.load();
    this.listenerLoader.load();
    this.scenarioLoader.load();

    var teamsBackup = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "team_registry_backup.bin");
    var playersBackup = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "player_registry_backup.bin");

    if(!teamsBackup.exists() || !playersBackup.exists()) { return; }

    try {
      Map<String, UhcTeam> teams = SaveLoad.load(teamsBackup.getAbsolutePath());
      uhcTeamRegistry.setBackupTeams(teams);

      teamsBackup.deleteOnExit();

      Map<String, UhcPlayer> players = SaveLoad.load(playersBackup.getAbsolutePath());
      uhcPlayerRegistry.setPlayerMapBackup(players);

      playersBackup.deleteOnExit();
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

    var teamsBackup = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "team_registry_backup.bin");
    var playersBackup = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "player_registry_backup.bin");

    teamsBackup.delete();
    playersBackup.delete();
  }
}
