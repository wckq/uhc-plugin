package io.github.wickeddroid.plugin.scoreboard;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import io.github.wickeddroid.plugin.util.PluginUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.stream.Collectors;

public class ScoreboardGame extends ScoreboardCreator {

  private final UhcGame uhcGame;
  private final Game game;
  private final UhcPlayerRegistry uhcPlayerRegistry;

  public ScoreboardGame(
          final Scoreboard scoreboard,
          final Plugin plugin,
          final UhcGame uhcGame,
          final UhcPlayerRegistry uhcPlayerRegistry,
          final Game game
  ) {
    super(scoreboard.title(), plugin);

    this.uhcGame = uhcGame;
    this.uhcPlayerRegistry = uhcPlayerRegistry;
    this.game = game;

    for (final var line : scoreboard.game().lines()) {
      this.updateLine(player -> this.replaceVariables(line, player));
    }
  }

  @Override
  public Component replaceVariables(String text, Player player) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());
    final var currentEpisode = game.episodes().enabled() ? (int) Math.ceil((double) uhcGame.getCurrentTime()/((double) game.episodes().episodeDurationTicks() / 20 )) : -1;

    return MessageUtil.parseStringToComponent(text,
            Placeholder.parsed("player-kills", String.valueOf(uhcPlayer.getKills())),
            Placeholder.parsed("players-alive", String.valueOf(uhcPlayerRegistry.getPlayers().stream().filter(UhcPlayer::isAlive).toList().size())),
            Placeholder.parsed("world-border", String.valueOf(this.uhcGame.getWorldBorder())),
            Placeholder.parsed("game-time", PluginUtil.formatTime(this.uhcGame.getCurrentTime())),
            Placeholder.parsed("game-state", PluginUtil.formatState(this.uhcGame.getUhcGameState())),
            Placeholder.parsed("episode", String.valueOf(currentEpisode)),
            Placeholder.parsed("episode-time", PluginUtil.formatTimeEpisode(this.uhcGame.getCurrentTime()*20L, game.episodes().episodeDurationTicks(), game.episodes().reversedTimer()))
    );
  }
}
