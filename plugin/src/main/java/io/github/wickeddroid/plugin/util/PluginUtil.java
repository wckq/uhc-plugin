package io.github.wickeddroid.plugin.util;


import java.util.Random;

public class PluginUtil {

  public static final Random RANDOM = new Random();
  public static final String OVERRIDE_LOBBY_PROTECTION_PERMISSION = "uhc.override_lobby_protection_permission";

  private PluginUtil() {

  }

  public static String formatTime(final long totalSecs){
    final var hours = (int) totalSecs / 3600;
    final var minutes = (int) (totalSecs % 3600) / 60;
    final var seconds = (int) totalSecs % 60;

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }
}
