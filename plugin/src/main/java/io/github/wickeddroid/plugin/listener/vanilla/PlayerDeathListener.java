package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.hook.DiscordWebhook;
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

import java.awt.*;
import java.io.IOException;

public class PlayerDeathListener implements Listener {

  @Inject
  private UhcPlayerRegistry uhcPlayerRegistry;
  @Inject
  private UhcTeamManager uhcTeamManager;
  @Inject
  private UhcGame uhcGame;

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

    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.PLAYER, 1.0F, 1.0F)));

    uhcPlayer.setAlive(false);

    player.setGameMode(GameMode.SPECTATOR);

    final var webhook = new DiscordWebhook("https://discord.com/api/webhooks/1119722451983667280/mGiZfpqKEAMogyFVv1jhO8ipRKSngclj3kwAfO3RW5lpwC2Qi6QBsbn1n0MuTZn6yRdS");

    webhook.addEmbed(new DiscordWebhook
            .EmbedObject()
            .setTitle("Muerte de " + player.getName())
            .setDescription(event.getDeathMessage())
            .setColor(Color.RED));

    try {
      webhook.execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
