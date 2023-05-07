package io.github.wickeddroid.plugin.listener.custom;

import io.github.wickeddroid.plugin.event.game.GameTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameTickListener implements Listener {
  @EventHandler
  public void onGameTick(final GameTickEvent event) {
    final var time = event.getTime();

    Bukkit.getLogger().info(time + "");
  }
}
