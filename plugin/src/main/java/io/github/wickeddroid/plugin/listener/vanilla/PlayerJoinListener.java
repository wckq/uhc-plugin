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
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectAll;

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
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var playerName = player.getName();

    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(playerName);

    if (uhcPlayer == null) {
      if (this.uhcGame.getUhcGameState() != UhcGameState.WAITING) {
        player.setGameMode(GameMode.SPECTATOR);
      }

      this.uhcPlayerRegistry.createPlayer(player.getUniqueId(), playerName);
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
      if(backup.resistance.contains(playerName) && uhcGame.getUhcGameState() != UhcGameState.MEETUP) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 25, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 25, false, false, false));

        backup.resistance.remove(playerName);
      }
    }
  }
}
