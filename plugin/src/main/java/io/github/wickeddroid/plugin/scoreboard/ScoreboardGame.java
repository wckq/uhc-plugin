package io.github.wickeddroid.plugin.scoreboard;

import org.bukkit.plugin.Plugin;

public class ScoreboardGame extends ScoreboardCreator {
  public ScoreboardGame(Scoreboard scoreboard, Plugin plugin) {
    super(scoreboard.title(), plugin);

    for (final var line : scoreboard.game().lines()) {
      this.updateLine(player -> this.replaceVariables(line, player));
    }
  }
}
