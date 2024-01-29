package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.UhcPlugin;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;

import java.awt.*;
import java.io.IOException;

@InjectAll
public class PlayerDeathListener implements Listener {

  private UhcPlayerRegistry uhcPlayerRegistry;
  private UhcTeamManager uhcTeamManager;
  private UhcGame uhcGame;
  private Game game;
  private UhcPlugin plugin;
  private Messages messages;
  private MessageHandler messageHandler;

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    final var player = event.getPlayer();

    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());
    final var uhcTeam = this.uhcTeamManager.getTeamByPlayer(player.getName());

    if (this.uhcGame == null) {
      return;
    }

    if(this.uhcGame.getUhcGameState() == UhcGameState.WAITING || this.uhcGame.getUhcGameState() == UhcGameState.FINISH) { return; }

    final var playerKiller = player.getKiller();

    if (playerKiller != null) {
      final var uhcKiller = this.uhcPlayerRegistry.getPlayer(playerKiller.getName());

      if (uhcKiller != null) {
        uhcKiller.incrementKills();
      }
    }

    if (uhcTeam != null) {
      Bukkit.getScheduler().runTaskLater(plugin, ()-> {
        uhcTeam.decrementPlayersAlive();

        if (uhcTeam.getPlayersAlive() <= 0) {
          uhcTeamManager.removeTeam(player.getUniqueId());

          messageHandler.sendGlobal(messages.team().teamEliminated(), uhcTeam.getTeam().getColor() + uhcTeam.getName());
        }
      }, 30L);
    }

    if (uhcPlayer == null) {
      return;
    }

    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.PLAYER, 1.0F, 1.0F)));

    uhcPlayer.setAlive(false);

    Bukkit.getScheduler().runTaskLater(plugin, ()-> {
      player.setGameMode(game.spectatorsEnabled() ? GameMode.SPECTATOR : GameMode.ADVENTURE);
    },40L);
  }
}
