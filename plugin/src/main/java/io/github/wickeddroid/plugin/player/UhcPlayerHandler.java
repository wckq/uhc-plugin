package io.github.wickeddroid.plugin.player;

import io.github.wickeddroid.plugin.game.UhcGameManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

public class UhcPlayerHandler {
  @Inject private UhcGameManager uhcGameManager;

  public void scatterPlayer(
          final Player player,
          final boolean laterScatter,
          final World world
  ) {
    try {
      uhcGameManager.scatterPlayer(player, laterScatter, world);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
