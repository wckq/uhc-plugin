package io.github.wickeddroid.plugin.listener.vanilla;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.Teams;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.inject.InjectAll;

@InjectAll
public class AsyncChatListener implements Listener {
  private Messages messages;
  private MessageHandler messageHandler;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private UhcTeamManager uhcTeamManager;
  private UhcGame uhcGame;
  private Teams teams;

  @EventHandler
  public void onAsyncChat(final AsyncChatEvent event) {
    final var player = event.getPlayer();
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());

    if (uhcPlayer == null) {
      return;
    }

    final var uhcTeam = uhcPlayer.getUhcTeam();
    final var message = PlainTextComponentSerializer.plainText().serialize(event.message());

    if(message.startsWith(teams.fastChatTogglePrefix())) {
      if(uhcTeam == null) { return; }

      if(uhcPlayer.isTeamChat()) {
        event.message(Component.text(message.replaceFirst(teams.fastChatTogglePrefix(), "")));
      } else {
        event.setCancelled(true);
        this.uhcTeamManager.sendMessageTeam(player, Component.text(message.replaceFirst(teams.fastChatTogglePrefix(), "")));
      }


      return;
    }

    if (!uhcPlayer.isTeamChat()) {
      return;
    }

    if (uhcTeam == null) {
      uhcPlayer.setTeamChat(false);
      this.messageHandler.send(player, this.messages.team().doesNotExist());
      return;
    }

    event.setCancelled(true);

    this.uhcTeamManager.sendMessageTeam(player, event.message());
  }
}
