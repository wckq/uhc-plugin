package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.DefaultUhcGame;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.team.UhcTeamHandler;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "team", permission = "prevention.permission")
public class CommandTeam implements CommandClass {

  @Inject private UhcTeamHandler uhcTeamHandler;
  @Inject private UhcTeamManager uhcTeamManager;
  @Inject private UhcGame uhcGame;
  @Inject private MessageHandler messageHandler;
  @Inject private Messages messages;

  @Command(names = "create")
  public void create(
          final @Sender Player sender,
          final @OptArg String name
  ) {
    if(!uhcGame.isOwnTeamsEnabled()) {
      this.messageHandler.send(sender, this.messages.team().ownTeamsHasNotEnabled());
      return;
    }

    this.uhcTeamManager.createTeam(sender, name);
  }

  @Command(names = "disband")
  public void disband(final @Sender Player sender) {
    if(!uhcGame.isOwnTeamsEnabled()) {
      this.messageHandler.send(sender, this.messages.team().ownTeamsHasNotEnabled());
      return;
    }

    this.uhcTeamManager.removeTeam(sender.getUniqueId());
  }

  @Command(names = "invite")
  public void invite(
          final @Sender Player sender,
          final Player target
  ) {
    if(!uhcGame.isOwnTeamsEnabled()) {
      this.messageHandler.send(sender, this.messages.team().ownTeamsHasNotEnabled());
      return;
    }

    this.uhcTeamHandler.invitePlayerToTeam(sender, target);
  }

  @Command(names = "accept")
  public void accept(
          final @Sender Player sender,
          final Player target
  ) {
    if(!uhcGame.isOwnTeamsEnabled()) {
      this.messageHandler.send(sender, this.messages.team().ownTeamsHasNotEnabled());
      return;
    }

    this.uhcTeamHandler.addPlayerToTeam(target, sender, false);
  }

  @Command(names = "leave")
  public void leave(final @Sender Player sender) {
    if(!uhcGame.isOwnTeamsEnabled()) {
      this.messageHandler.send(sender, this.messages.team().ownTeamsHasNotEnabled());
      return;
    }

    this.uhcTeamHandler.removePlayerOfTeam(sender);
  }
}
