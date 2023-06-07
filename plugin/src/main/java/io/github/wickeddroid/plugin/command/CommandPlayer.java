package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.menu.scenario.ScenariosEnabledInventory;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

@InjectAll
public class CommandPlayer implements CommandClass {

  private ScenariosEnabledInventory scenariosInventory;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private ScenarioManager scenarioManager;
  private UhcGameHandler uhcGameHandler;
  private UhcTeamManager uhcTeamManager;
  private MessageHandler messageHandler;
  private Messages messages;

  @Command(names = {"teamchat", "tc", "chat", "c"})
  public void teamChat(final @Sender Player sender) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(sender.getName());

    if (uhcPlayer == null) {
      return;
    }

    if (uhcTeamManager.getTeamByPlayer(uhcPlayer.getName()) == null) {
      this.messageHandler.send(sender, this.messages.team().doesNotExist());
      return;
    }

    uhcPlayer.setTeamChat(!uhcPlayer.isTeamChat());

    this.messageHandler.send(sender, uhcPlayer.isTeamChat()
            ? this.messages.other().teamChatOn()
            : this.messages.other().teamChatOff());
  }

  @Command(names = "scenarios")
  public void scenarios(final @Sender Player sender) {
    sender.openInventory(scenariosInventory.createInventory());
  }
}
