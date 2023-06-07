package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "team")
public class CommandTeam implements CommandClass {

  @Inject
  private MessageHandler messageHandler;
  @Inject
  private Messages messages;
  @Inject
  private UhcGame uhcGame;

  @Command(names = "enable")
  public void enable(
          final @Sender Player sender,
          final boolean enable
  ) {
    this.uhcGame.setTeamEnabled(enable);

    this.messageHandler.send(sender, enable
            ? this.messages.team().enableTeam()
            : this.messages.team().disableTeam());
  }

  @Command(names = "size")
  public void size(
          final @Sender Player sender,
          final int teamSize
  ) {
    this.uhcGame.setTeamSize(teamSize);

    this.messageHandler.send(sender, this.messages.team().changeTeamSize(), String.valueOf(teamSize));
  }
}
