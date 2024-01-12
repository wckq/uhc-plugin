package io.github.wickeddroid.plugin.util;


import io.github.wickeddroid.api.game.UhcGameState;
import org.bukkit.Bukkit;

import java.util.Random;
import java.util.regex.Pattern;

public class PluginUtil {

  public static final Random RANDOM = new Random();
  public static final String OVERRIDE_LOBBY_PROTECTION_PERMISSION = "uhc.override_lobby_protection_permission";
  private static final String version = Bukkit.getServer().getClass().getName().split(Pattern.quote("."))[3];
  public static final Byte versionNumber = Byte.parseByte(version.split(Pattern.quote("_"))[1]);;
  public static final Byte patchNumber = Byte.parseByte(version.split(Pattern.quote("_"))[2].substring(1));;

  private PluginUtil() {
  }

  public static String formatTime(final long totalSecs){
    final var hours = (int) totalSecs / 3600;
    final var minutes = (int) (totalSecs % 3600) / 60;
    final var seconds = (int) totalSecs % 60;

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
