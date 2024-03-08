package io.github.wickeddroid.plugin.player;

import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.world.ScatterTask;
import io.github.wickeddroid.plugin.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

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
