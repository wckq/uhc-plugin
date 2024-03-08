package io.github.wickeddroid.plugin.thread;

import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.api.event.player.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.game.DefaultUhcEventManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ScatterThread implements Runnable {

  private final Location location;
  private Player player;
  private UhcTeam uhcTeam;
  private boolean laterScatter;

  public ScatterThread(
          final Player player,
          final Location location,
          final boolean laterScatter) {
    this.player = player;
    this.location = location;
    this.laterScatter = laterScatter;
  }

  public ScatterThread(
          final UhcTeam uhcTeam,
          final Location location,
          final boolean laterScatter) {
    this.uhcTeam = uhcTeam;
    this.location = location;
    this.laterScatter = laterScatter;
  }

  @Override
  public void run() {
    if (uhcTeam == null) {
      UhcEventManager.fireScatter(player, location, laterScatter);
      return;
    }

    for (final var member : this.uhcTeam.getMembers()) {
      final var player = Bukkit.getPlayer(member);

      if (player == null || !player.isOnline()) {
        this.uhcTeam.removeMember(member);
        return;
      }

      UhcEventManager.fireScatter(player, location, laterScatter);
    }
  }
}

