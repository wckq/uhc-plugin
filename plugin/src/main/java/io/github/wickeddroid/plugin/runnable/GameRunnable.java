package io.github.wickeddroid.plugin.runnable;

import io.github.wickeddroid.api.game.UhcGame;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

@Singleton
public class GameRunnable extends BukkitRunnable {
  @Inject private UhcGame uhcGame;

  @Override
  public void run() {
    final var currentTime = (int) (Math.floor((System.currentTimeMillis() - this.uhcGame.getStartTime()) / 1000.0));

    if (currentTime == this.uhcGame.getTimeForPvp()) {
      this.uhcGame.setPvp(true);
    }

    if (currentTime == this.uhcGame.getWorldBorder()) {

    }

    this.uhcGame.setCurrentTime(currentTime);
  }
}
