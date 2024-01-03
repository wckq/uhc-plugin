package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.DefaultUhcGame;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.team.Teams;
import io.github.wickeddroid.plugin.team.UhcTeamHandler;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "team", permission = "prevention.permission")
public class CommandTeam implements CommandClass {

  @Inject private UhcTeamHandler uhcTeamHandler;
  @Inject private UhcTeamManager uhcTeamManager;
  @Inject private UhcTeamRegistry uhcTeamRegistry;
  @Inject private UhcGame uhcGame;
  @Inject private MessageHandler messageHandler;
  @Inject private Messages messages;
  @Inject private Teams teams;

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

  @Command(names = "set-name")
  public void setName(final @Sender Player sender, @Named("name") String name) {
    if(!teams.customization().allowCustomName()) {
      messageHandler.send(sender, messages.team().changeTeamNameError());
      return;
    }

    if(name.length() > teams.customization().maxNameLength()) {
      messageHandler.send(sender, messages.team().maxLengthReached(), String.valueOf(teams.customization().maxNameLength()));
      return;
    }

    var team = uhcTeamRegistry.getTeam(sender.getName());

    if(team == null) {
      messageHandler.send(sender, messages.team().playerDoesNotTeamExist());
      return;
    }

    team.setName(name);

    messageHandler.send(sender, messages.team().settingChanged(), name);
  }

  @Command(names = "set-prefix")
  public void setPrefix(final @Sender Player sender, @Named("name") String prefix) {
    if(!teams.customization().allowCustomPrefix()) {
      messageHandler.send(sender, messages.team().changeTeamPrefixError());
      return;
    }

    if(prefix.length() > teams.customization().maxPrefixLength()) {
      messageHandler.send(sender, messages.team().maxLengthReached(), String.valueOf(teams.customization().maxPrefixLength()));
      return;
    }

    var team = uhcTeamRegistry.getTeam(sender.getName());

    if(team == null) {
      messageHandler.send(sender, messages.team().playerDoesNotTeamExist());
      return;
    }

    team.getTeam().prefix(MessageUtil.parseStringToComponent("["+prefix+"]")
            .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
            .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.FALSE)
            .decoration(TextDecoration.UNDERLINED, TextDecoration.State.FALSE));

    messageHandler.send(sender, messages.team().settingChanged(), prefix);
  }
}
