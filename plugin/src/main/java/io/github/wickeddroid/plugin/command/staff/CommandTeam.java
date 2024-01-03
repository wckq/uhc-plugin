package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
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
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

import java.util.Objects;

@Command(names = "team")
public class CommandTeam implements CommandClass {

  @Inject
  private UhcTeamManager uhcTeamManager;
  @Inject
  private UhcTeamRegistry uhcTeamRegistry;
  @Inject
  private MessageHandler messageHandler;
  @Inject
  private UhcTeamHandler uhcTeamHandler;
  @Inject
  private Messages messages;
  @Inject
  private UhcGame uhcGame;
  @Inject
  private Teams teams;

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

  @Command(names = "enable-own")
  public void enableOwn(final @Sender Player sender,
                            final boolean enable) {
    this.uhcGame.setOwnTeamsEnabled(enable);

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

  @Command(names = "randomize")
  public void randomize(final @Sender Player sender) {
    this.uhcTeamManager.randomizeTeams(sender, this.uhcGame.getTeamSize());
  }

  @Command(names = "remove")
  public void remove(final OfflinePlayer target) {
    this.uhcTeamManager.removeTeam(target.getUniqueId());
  }


  @Command(names = "remove-all")
  public void removeAll() {
    this.uhcTeamRegistry.getTeams().forEach(uhcTeam -> this.uhcTeamManager.removeTeam(
            Objects.requireNonNull(Bukkit.getPlayer(uhcTeam.getLeader()).getUniqueId())
    ));
  }

  @Command(names = "force-join")
  public void forceJoin(
          final @Sender Player sender,
          final Player target,
          final Player leader
  ) {
    this.uhcTeamHandler.addPlayerToTeam(leader, target, true);
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
