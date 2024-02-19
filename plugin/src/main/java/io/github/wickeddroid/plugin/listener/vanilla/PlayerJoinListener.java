package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.game.Game;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardEndGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardGame;
import io.github.wickeddroid.plugin.scoreboard.ScoreboardLobby;
import io.github.wickeddroid.plugin.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;

import java.util.stream.Collectors;

@InjectAll
public class PlayerJoinListener implements Listener {

  private UhcPlayerRegistry uhcPlayerRegistry;
  private ScoreboardEndGame scoreboardEndGame;
  private ScoreboardLobby scoreboardLobby;
  private ScoreboardGame scoreboardGame;
  private UhcGame uhcGame;
  private Game game;
  private Backup backup;

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent event) {
    var size = uhcGame.getPlayersSize();
    var online = Bukkit.getOnlinePlayers().stream().filter(p -> !p.isOp()).toList().size();

    var whitelist = Bukkit.getWhitelistedPlayers().stream().map(oP -> oP.getName()).toList();
    var whitelistOn = Bukkit.hasWhitelist();

    if(whitelistOn && !whitelist.contains(event.getPlayer().getName())) {
      event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, MessageUtil.parseStringToComponent("<red>No estás en la whitelist."));
      return;
    }

    if(online >= size) {
      event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, MessageUtil.parseStringToComponent("<dark_red>El server está lleno."));
      return;
    }

    if(event.getPlayer().isBanned()) {
      event.disallow(PlayerLoginEvent.Result.KICK_BANNED, MessageUtil.parseStringToComponent("<dark_red>Estás baneado del servidor."));
      return;
    }

    if(event.getPlayer().isOnline()) {
      event.disallow(PlayerLoginEvent.Result.KICK_OTHER, MessageUtil.parseStringToComponent("<dark_red>¡Ya estás conectado al servidor!"));
      return;
    }

    var ip = event.getAddress().getHostAddress();
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(event.getPlayer().getName());

    if(uhcPlayer != null) {
      if(!ip.equalsIgnoreCase(uhcPlayer.getSession().IP()) && (System.currentTimeMillis() - uhcPlayer.getSession().lastConnect() > 300000)) {
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, MessageUtil.parseStringToComponent("<dark_red>Por seguridad del servidor se ha prevenido el ingreso."));
        return;
      }
    }

    event.allow();
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var playerName = player.getName();

    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerName);

    if (uhcPlayer == null) {
      if (this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
        player.setGameMode(GameMode.SPECTATOR);
      }

      this.uhcPlayerRegistry.createPlayer(player.getUniqueId(), playerName, player.getAddress().getAddress().getHostAddress());
    } else {
      uhcPlayer.getSession().setIP(player.getAddress().getAddress().getHostAddress());
      uhcPlayer.getSession().updateLastConnect();
    }

    if (this.uhcGame.getUhcGameState() == UhcGameState.WAITING) {
      this.scoreboardLobby.getSidebar().addViewer(player);
    } else if (this.uhcGame.getUhcGameState() == UhcGameState.FINISH) {
      this.scoreboardEndGame.getSidebar().addViewer(player);
    } else {
      this.scoreboardGame.getSidebar().addViewer(player);
    }

    player.sendPlayerListHeaderAndFooter(MessageUtil.parseStringToComponent(game.playerList().header()), MessageUtil.parseStringToComponent(game.playerList().footer()));

    if(uhcGame.loadedBackup()) {
      if(uhcGame.getBackupPlayers().contains(playerName)) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 25, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 25, false, false, false));

        uhcGame.getBackupPlayers().remove(playerName);
      }
    }
  }
}
