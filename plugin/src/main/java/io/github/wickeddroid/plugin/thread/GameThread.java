package io.github.wickeddroid.plugin.thread;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.message.announcements.Announcements;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.mariuszgromada.math.mxparser.Expression;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Singleton;

import java.util.concurrent.atomic.AtomicBoolean;

@InjectAll
@Singleton
public class GameThread implements Runnable {

  private Worlds worlds;
  private UhcGame uhcGame;
  private UhcGameHandler uhcGameHandler;
  private UhcGameManager uhcGameManager;
  private UhcTeamRegistry uhcTeamRegistry;
  private Announcements announcements;
  private ScenarioManager scenarioManager;

  @Override
  public void run() {
    final var currentTime = (int) (Math.floor((System.currentTimeMillis() - this.uhcGame.getStartTime()) / 1000.0));

    this.uhcGame.setCurrentTime(currentTime);

    if (currentTime == this.uhcGame.getTimeForPvp()) {
      this.uhcGameHandler.changePvp(true);
    }

    if (currentTime == this.uhcGame.getTimeForMeetup()) {
      this.uhcGameManager.startMeetup();
    }

    if (currentTime >= this.uhcGame.getTimeForMeetup()) {
      final var world = Bukkit.getWorld(this.worlds.worldName());

      if (world == null) {
        return;
      }

      this.uhcGame.setWorldBorder((int) world.getWorldBorder().getSize() / 2);
    }

    if(this.uhcTeamRegistry.getTeams().stream().filter(UhcTeam::isAlive).toList().size() == 1 && uhcGame.isTeamEnabled()) {
      this.uhcGameManager.endGame();
    }



    announcements.announcements().forEach(a -> {

      AtomicBoolean verify = new AtomicBoolean(false);
      a.scenarios().forEach(s -> {
        if(s.startsWith("!") && !scenarioManager.isEnabled(s.replace("!",""))) { return; }

        if(scenarioManager.isEnabled(s)) { return; }

        verify.set(true);
      });

      if(verify.get()) { return; }

      var time = a.time()
              .replaceAll("%pvp%", String.valueOf(this.uhcGame.getTimeForPvp()))
              .replaceAll("%meetup%", String.valueOf(this.uhcGame.getTimeForMeetup()))
              .replaceAll("%wb-delay%", String.valueOf(this.worlds.border().worldBorderDelay()))
              .replaceAll("%wb-meetup-delay%", String.valueOf(this.worlds.border().worldBorderDelayAfterMeetup()));
      Expression ex = new Expression(time);

      var seconds = ex.calculate();

      if(Double.isNaN(seconds)) {
        Bukkit.getLogger().severe("Invalid announcement time");
        return;
      }

      if(currentTime != seconds) {
        return;
      }


      Bukkit.getOnlinePlayers().forEach(p -> {
        p.showTitle(a.title());
        p.playSound(a.sound());
      });
    });
  }
}
