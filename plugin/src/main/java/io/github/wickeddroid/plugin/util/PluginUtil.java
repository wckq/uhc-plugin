package io.github.wickeddroid.plugin.util;


import io.github.wickeddroid.api.game.UhcGameState;
import org.bukkit.Bukkit;

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
