package io.github.wickeddroid.api.event;

import io.github.wickeddroid.api.event.game.GameEndEvent;
import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.event.player.PlayerIronmanStatusChangeEvent;
import io.github.wickeddroid.api.event.player.PlayerScatteredEvent;
import io.github.wickeddroid.api.event.scenario.ScenarioOptionValueChangeEvent;
import io.github.wickeddroid.api.event.team.TeamEliminatedEvent;
import io.github.wickeddroid.api.event.team.TeamPlayerJoinEvent;
import io.github.wickeddroid.api.event.team.TeamPlayerLeaveEvent;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.team.UhcTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface UhcEventManager {

   static GameStartEvent fireGameStart() {
       var event = new GameStartEvent();
       fire(event);
       return event;
   }
   static GameEndEvent fireGameEnd() {
       var event = new GameEndEvent();
       fire(event);
       return event;
   }

   static PlayerScatteredEvent fireScatter(Player player, Location location, boolean laterScatter) {
       var event = new PlayerScatteredEvent(player, location, laterScatter);
       fire(event);
       return event;
   }
   static PlayerIronmanStatusChangeEvent firePlayerIronmanStatusChange(@NotNull Player player, @NotNull PlayerIronmanStatusChangeEvent.Status currentStatus, @NotNull PlayerIronmanStatusChangeEvent.Status oldStatus)
   {
       var event = new PlayerIronmanStatusChangeEvent(player, currentStatus, oldStatus);
       fire(event);
       return event;
   }

   static <T> ScenarioOptionValueChangeEvent<T> fireScenarioOptionValueChange(@NotNull Option<T> option, @NotNull OptionValue<T> value) {
       var event = new ScenarioOptionValueChangeEvent<>(option, value);
       fire(event);
       return event;
   }

   static TeamEliminatedEvent fireTeamEliminated(@NotNull UhcTeam uhcTeam) {
       var event = new TeamEliminatedEvent(uhcTeam);
       fire(event);
       return event;
   }
   static TeamPlayerJoinEvent fireTeamPlayerJoin(@NotNull Player player, @NotNull UhcTeam uhcTeam) {
       var event = new TeamPlayerJoinEvent(player, uhcTeam);
       fire(event);
       return event;
   }
   static TeamPlayerLeaveEvent fireTeamPlayerLeave(@NotNull Player player, @NotNull UhcTeam uhcTeam) {
       var event = new TeamPlayerLeaveEvent(player, uhcTeam);
       fire(event);
       return event;
   }

   private static void fire(UhcEvent event) {
       new BukkitRunnable() {
           @Override
           public void run() {
               Bukkit.getPluginManager().callEvent(event);
           }
       }.runTask(Bukkit.getServer().getPluginManager().getPlugin("uhc-plugin"));
   }
}
