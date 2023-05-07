package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import team.unnamed.inject.Inject;

public class PlayerDeathListener implements Listener {
  @Inject private UhcPlayerRegistry uhcPlayerRegistry;
  @Inject private UhcTeamManager uhcTeamManager;
  @Inject private UhcGame uhcGame;

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    final var player = event.getPlayer();

    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());
    final var uhcTeam = this.uhcTeamManager.getTeamByPlayer(player.getName());

    if (this.uhcGame == null) {
      return;
    }

    if (this.uhcGame.getUhcGameState() == UhcGameState.WAITING
            || this.uhcGame.getUhcGameState() == UhcGameState.FINISH) {
      return;
    }

    final var playerKiller = player.getKiller();

    if (playerKiller != null) {
      final var uhcKiller = this.uhcPlayerRegistry.getPlayer(playerKiller.getName());

      if (uhcKiller == null) {
        return;
      }

      uhcKiller.incrementKills();
    }

    if (uhcTeam != null) {
      uhcTeam.decrementPlayersAlive();

      if (uhcTeam.getPlayersAlive() != 0) {
        return;
      }

      uhcTeamManager.removeTeam(player);
    }

    if (uhcPlayer == null) {
      return;
    }

    uhcPlayer.setAlive(false);

    player.setGameMode(GameMode.SPECTATOR);
  }
}
