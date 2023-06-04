package io.github.wickeddroid.plugin.player;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.util.LocationUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

public class UhcPlayerHandler {

  @Inject private Worlds worlds;
  @Inject private UhcGame uhcGame;

  public void scatterPlayer(final Player player) {
    final var location = LocationUtil.generateRandomLocation(this.uhcGame, this.worlds.worldName());


  }
}
