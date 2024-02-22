package io.github.wickeddroid.plugin.util;


import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.scoreboard.Scoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

public class PluginUtil {

  public static final Random RANDOM = new Random();
  public static final String OVERRIDE_LOBBY_PROTECTION_PERMISSION = "uhc.override_lobby_protection_permission";
  private static final String version = Bukkit.getServer().getClass().getName().split(Pattern.quote("."))[3];
  public static final Byte versionNumber = Byte.parseByte(version.split(Pattern.quote("_"))[1]);;
  public static final Byte patchNumber = Byte.parseByte(version.split(Pattern.quote("_"))[2].substring(1));;

  private PluginUtil() {
  }

  @SafeVarargs
  public static <E> List<E> appendList(Collection<E> list, E... e) {
    var l2 = new ArrayList<>(list);
    l2.addAll(Arrays.asList(e));

    return l2;
  }
  public static String formatTime(final long totalSecs){
    final var hours = (int) totalSecs / 3600;
    final var minutes = (int) (totalSecs % 3600) / 60;
    final var seconds = (int) totalSecs % 60;

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public static String formatTimeEpisode(final long gameTicks, long episodeDurationTicks, boolean reversed) {
    long totalSeconds = gameTicks / 20;
    long episodeDurationSeconds = episodeDurationTicks / 20;

    long remainingSeconds = !reversed ? totalSeconds % episodeDurationSeconds : episodeDurationSeconds - totalSeconds % episodeDurationSeconds;

    long minutes = remainingSeconds / 60;
    long seconds = remainingSeconds % 60;

    return String.format("%02d:%02d", minutes, seconds);
  }

  public static Component formatTeamHP(Player player, UhcTeam uhcTeam, Scoreboard.Game scoreboardGame) {
    Component c = Component.text("");

    if(uhcTeam == null) { return c; }

    if(scoreboardGame.displayDeadPlayers() ? uhcTeam.getMembers().stream().filter(p -> !p.equalsIgnoreCase(player.getName())).toList().size()-1 > scoreboardGame.maxTeamDisplay() : uhcTeam.getPlayersAlive()-1 > scoreboardGame.maxTeamDisplay()) {
      return c;
    }
    var it = uhcTeam.getMembers().iterator();

    while (it.hasNext()) {
      var name = it.next();
      var pl = Bukkit.getOfflinePlayer(name);

      if(name.equalsIgnoreCase(player.getName())) { continue; }

      if(!scoreboardGame.displayDeadPlayers() && !pl.isOnline()) {
        continue;
      }

      if(!pl.isOnline()) {
        c = c.append(
                MessageUtil.parseStringToComponent(
                        scoreboardGame.teamDisplay(),
                        Placeholder.parsed("player-name", name),
                        Placeholder.parsed("player-hp", ""),
                        Placeholder.parsed("status-symbol", scoreboardGame.symbols().disconnected())
                )
        );

        continue;
      }

      var onlinePlayer = pl.getPlayer();
      if(onlinePlayer.getGameMode() != GameMode.SURVIVAL) {
        if(!scoreboardGame.displayDeadPlayers()) { continue; }

        c = c.append(
                MessageUtil.parseStringToComponent(
                        scoreboardGame.teamDisplay(),
                        Placeholder.parsed("player-name", name),
                        Placeholder.parsed("player-hp", ""),
                        Placeholder.parsed("status-symbol", scoreboardGame.symbols().dead())
                )
        );
      } else {
        c = c.append(
                MessageUtil.parseStringToComponent(
                        scoreboardGame.teamDisplay(),
                        Placeholder.parsed("player-name", name),
                        Placeholder.parsed("player-hp", String.valueOf(Math.round(player.getHealth()))),
                        Placeholder.parsed("status-symbol", scoreboardGame.symbols().alive())
                )
        );
      }
    }

    return c;
  }

  public static String formatState(UhcGameState gameState) {
    switch (gameState) {
      case PLAYING -> {
        return "Pacto";
      }
      case PVP -> {
        return "PvP";
      }
      case MEETUP -> {
        return "Meetup";
      }
      default -> {
        return "Desconocido";
      }
    }
  }
}
