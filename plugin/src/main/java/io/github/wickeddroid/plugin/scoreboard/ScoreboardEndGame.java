package io.github.wickeddroid.plugin.scoreboard;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import io.github.wickeddroid.plugin.util.PluginUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Comparator;

public class ScoreboardEndGame extends ScoreboardCreator {

  private final UhcGame uhcGame;
  private final UhcPlayerRegistry uhcPlayerRegistry;
  private final UhcTeamRegistry uhcTeamRegistry;
  public ScoreboardEndGame(Scoreboard scoreboard, Plugin plugin, final UhcGame uhcGame, final UhcPlayerRegistry uhcPlayerRegistry, UhcTeamRegistry uhcTeamRegistry) {
    super(scoreboard.title(), plugin);

    this.uhcGame = uhcGame;
    this.uhcPlayerRegistry = uhcPlayerRegistry;
    this.uhcTeamRegistry = uhcTeamRegistry;

    for (final var line : scoreboard.end().lines()) {
      this.updateLine(player -> this.replaceVariables(line, player));
    }
  }

  @Override
  public Component replaceVariables(String text, Player player) {
    final var winner = this.uhcTeamRegistry.getTeams().stream().filter(UhcTeam::isAlive).findAny().orElse(null);
    final var topKills = this.uhcPlayerRegistry.getPlayers().stream().sorted(Comparator.comparingInt(UhcPlayer::getKills).reversed()).toList();

    return MessageUtil.parseStringToComponent(text,
            Placeholder.parsed("winner-team", winner == null ? "N/A" : winner.getName()),
            Placeholder.parsed("top-kills", topKills.get(0) != null ? topKills.get(0).getName() + " (%s)".formatted(topKills.get(0).getKills()) : "N/A"),
            Placeholder.parsed("ironman", uhcGame.ironman() == null ? "N/A" : uhcGame.ironman()),
            Placeholder.parsed("paperman", uhcGame.paperman() == null ? "N/A" : uhcGame.paperman()),
            Placeholder.parsed("game-duration", PluginUtil.formatTime(this.uhcGame.getCurrentTime()))
    );
  }
}
