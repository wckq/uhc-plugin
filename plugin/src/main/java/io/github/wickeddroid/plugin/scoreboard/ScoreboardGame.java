package io.github.wickeddroid.plugin.scoreboard;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.util.MessageUtils;
import io.github.wickeddroid.plugin.util.PluginUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScoreboardGame extends ScoreboardCreator {
  private final UhcGame uhcGame;
  private final UhcPlayerRegistry uhcPlayerRegistry;

  public ScoreboardGame(
          final Scoreboard scoreboard,
          final Plugin plugin,
          final UhcGame uhcGame,
          final UhcPlayerRegistry uhcPlayerRegistry
  ) {
    super(scoreboard.title(), plugin);

    this.uhcGame = uhcGame;
    this.uhcPlayerRegistry = uhcPlayerRegistry;

    for (final var line : scoreboard.game().lines()) {
      this.updateLine(player -> this.replaceVariables(line, player));
    }
  }

  @Override
  public Component replaceVariables(String text, Player player) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());

    return MessageUtils.parseStringToComponent(text,
            Placeholder.parsed("player-kills", String.valueOf(uhcPlayer.getKills())),
            Placeholder.parsed("players-alive", String.valueOf(Bukkit.getOnlinePlayers().size())),
            Placeholder.parsed("world-border", String.valueOf(this.uhcGame.getWorldBorder())),
            Placeholder.parsed("game-time", PluginUtils.formatTime(this.uhcGame.getCurrentTime()))
    );
  }
}
