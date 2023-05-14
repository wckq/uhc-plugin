package io.github.wickeddroid.plugin.runnable;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

@Singleton
public class GameRunnable extends BukkitRunnable {
  @Inject private Worlds worlds;
  @Inject private UhcGame uhcGame;
  @Inject private UhcGameHandler uhcGameHandler;

  @Override
  public void run() {
    final var currentTime = (int) (Math.floor((System.currentTimeMillis() - this.uhcGame.getStartTime()) / 1000.0));
    final var world = Bukkit.getWorld(this.worlds.worldName());

    this.uhcGame.setCurrentTime(currentTime);

    if (currentTime == this.uhcGame.getTimeForPvp()) {
      this.uhcGameHandler.changePvp(true);
    }

    if (currentTime == this.uhcGame.getTimeForMeetup()) {
      this.uhcGameHandler.startMeetup();
    }

    if (currentTime >= this.uhcGame.getTimeForMeetup()) {
      if (world == null) {
        return;
      }

      this.uhcGame.setWorldBorder((int) world.getWorldBorder().getSize() / 2);
    }
  }
}
