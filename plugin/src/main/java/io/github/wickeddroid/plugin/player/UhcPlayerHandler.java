package io.github.wickeddroid.plugin.player;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.event.game.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.util.LocationUtil;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

public class UhcPlayerHandler {

  @Inject private Worlds worlds;
  @Inject private UhcGame uhcGame;

  public void scatterPlayer(
          final Player player,
          final boolean laterScatter
  ) {
    final var location = LocationUtil.generateRandomLocation(uhcGame, worlds.worldName());

    if (location == null) {
      return;
    }

    uhcGame.addIronman(player.getName());

    Bukkit.getPluginManager().callEvent(new PlayerScatteredEvent(player, location, laterScatter));
  }
}
