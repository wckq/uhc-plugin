package io.github.wickeddroid.plugin.scoreboard;

import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScoreboardLobby extends ScoreboardCreator {
  private final UhcPlayerRegistry uhcPlayerRegistry;
  private final UhcTeamManager uhcTeamManager;

  public ScoreboardLobby(
          final Scoreboard scoreboard,
          final Plugin plugin,
          final UhcPlayerRegistry uhcPlayerRegistry,
          final UhcTeamManager uhcTeamManager
  ) {
    super(scoreboard.title(), plugin);

    this.uhcPlayerRegistry = uhcPlayerRegistry;
    this.uhcTeamManager = uhcTeamManager;

    for (final var line : scoreboard.lobby().lines()) {
      this.updateLine(player -> this.replaceVariables(line, player));
    }
  }

  @Override
  public Component replaceVariables(
          final String text,
          final Player player
  ) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());
    final var uhcTeam = this.uhcTeamManager.getTeamByPlayer(player.getName());

    return MessageUtil.parseStringToComponent(
            text,
            Placeholder.parsed("online-players", String.valueOf(Bukkit.getOnlinePlayers().size())),
            Placeholder.parsed("max-players", String.valueOf(Bukkit.getServer().getMaxPlayers())),
            Placeholder.parsed("team-name", uhcTeam == null ? "" : uhcTeam.getName()),
            Placeholder.parsed("team-owner", uhcTeam == null ? "" : uhcTeam.getLeader()),
            Placeholder.parsed("team-kills", uhcTeam == null ? "" : String.valueOf(uhcTeam.getKills())),
            Placeholder.parsed("kills", String.valueOf(uhcPlayer.getKills())));
  }
}
