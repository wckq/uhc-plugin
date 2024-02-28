package io.github.wickeddroid.plugin.player;

import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.event.player.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.util.LocationUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

public class UhcPlayerHandler {

  @Inject private Worlds worlds;
  @Inject private UhcGame uhcGame;
  @Inject private Game game;

  public void scatterPlayer(
          final Player player,
          final boolean laterScatter
  ) {
    final var location = LocationUtil.generateRandomLocation(uhcGame, worlds.worldName());

    if (location == null) {
      return;
    }

    UhcEventManager.fireScatter(player, location, laterScatter);

    if(laterScatter && !game.laterScatterIronman()) { return; }

    uhcGame.getIronmans().add(player.getName());
  }
}
