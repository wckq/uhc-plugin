package io.github.wickeddroid.plugin.thread;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

@Singleton
public class GameThread implements Runnable {

  @Inject private Worlds worlds;
  @Inject private UhcGame uhcGame;
  @Inject private UhcGameHandler uhcGameHandler;

  @Override
  public void run() {
    final var currentTime = (int) (Math.floor((System.currentTimeMillis() - this.uhcGame.getStartTime()) / 1000.0));

    this.uhcGame.setCurrentTime(currentTime);

    if (currentTime == this.uhcGame.getTimeForPvp()) {
      this.uhcGameHandler.changePvp(true);
    }

    if (currentTime == this.uhcGame.getTimeForMeetup()) {
      this.uhcGameHandler.startMeetup();
    }

    if (currentTime >= this.uhcGame.getTimeForMeetup()) {
      final var world = Bukkit.getWorld(this.worlds.worldName());

      if (world == null) {
        return;
      }

      this.uhcGame.setWorldBorder((int) world.getWorldBorder().getSize() / 2);
    }
  }
}
