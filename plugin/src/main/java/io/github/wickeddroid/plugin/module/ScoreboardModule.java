package io.github.wickeddroid.plugin.module;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scoreboard.Scoreboard;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardEndGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

public class ScoreboardModule extends AbstractModule {

  @Provides @Singleton
  public ScoreboardLobby lobbyScoreboardProvider(
          final Plugin plugin,
          final Scoreboard scoreboard,
          final UhcPlayerRegistry uhcPlayerRegistry,
          final UhcGame uhcGame
  ) {
    return new ScoreboardLobby(scoreboard, plugin, uhcPlayerRegistry, uhcGame);
  }

  @Provides @Singleton
  public ScoreboardGame gameScoreboardProvider(
          final Plugin plugin,
          final Scoreboard scoreboard,
          final UhcGame uhcGame,
          final UhcPlayerRegistry uhcPlayerRegistry,
          final Game game
          ) {
    return new ScoreboardGame(
            scoreboard,
            plugin,
            uhcGame,
            uhcPlayerRegistry,
            game
    );
  }
  @Provides @Singleton
  public ScoreboardEndGame endGameScoreboardProvider(
          final Plugin plugin,
          final Scoreboard scoreboard,
          final UhcGame uhcGame,
          final UhcPlayerRegistry uhcPlayerRegistry,
          final UhcTeamRegistry uhcTeamRegistry
          ) {
    return new ScoreboardEndGame(scoreboard, plugin, uhcGame, uhcPlayerRegistry, uhcTeamRegistry);
  }
}
