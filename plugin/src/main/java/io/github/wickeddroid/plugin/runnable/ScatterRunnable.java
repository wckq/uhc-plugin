package io.github.wickeddroid.plugin.runnable;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.event.game.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScatterRunnable extends BukkitRunnable {
  private final Location location;
  private Player player;
  private UhcTeam uhcTeam;

  public ScatterRunnable(
          final Player player,
          final Location location
  ) {
    this.player = player;
    this.location = location;
  }

  public ScatterRunnable(
          final UhcTeam uhcTeam,
          final Location location
  ) {
    this.uhcTeam = uhcTeam;
    this.location = location;
  }

  @Override
  public void run() {
    var scattered = 1;

    if (uhcTeam == null) {
      Bukkit.getPluginManager().callEvent(new PlayerScatteredEvent(player, location, scattered));
      ++scattered;
      return;
    }

    for (final var member : this.uhcTeam.getMembers()) {
      final var player = Bukkit.getPlayer(member);

      if (player == null || !player.isOnline()) {
        this.uhcTeam.removeMember(member);
        return;
      }

      Bukkit.getPluginManager().callEvent(new PlayerScatteredEvent(player, location, scattered));
      ++scattered;
    }
  }
}

