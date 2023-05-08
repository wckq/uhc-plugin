package io.github.wickeddroid.plugin.runnable;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

@Singleton
public class GameRunnable extends BukkitRunnable {
  @Inject private UhcGame uhcGame;
  @Inject private UhcGameHandler uhcGameHandler;

  @Override
  public void run() {
    final var currentTime = (int) (Math.floor((System.currentTimeMillis() - this.uhcGame.getStartTime()) / 1000.0));

    this.uhcGame.setCurrentTime(currentTime);

    if (currentTime == this.uhcGame.getTimeForPvp()) {
      this.uhcGameHandler.changePvp(true);
    }

    if (currentTime == this.uhcGame.getWorldBorder()) {
      this.uhcGameHandler.startMeetup();
    }
  }
}
