package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.team.Teams;
import io.github.wickeddroid.plugin.team.UhcTeamHandler;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Named;
import team.unnamed.commandflow.annotated.annotation.Sender;
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
  public void remove(final Player target) {
    this.uhcTeamManager.removeTeam(target.getUniqueId());
  }

  @Command(names = "remove-all")
  public void removeAll() {
    this.uhcTeamRegistry.getTeams().forEach(uhcTeam -> this.uhcTeamManager.removeTeam(
            Bukkit.getOfflinePlayer(uhcTeam.getLeader()).getUniqueId()
    ));
  }

  @Command(names = "force-join")
  public void forceJoin(
          final @Sender Player sender,
          final Player target,
          final Player leader
  ) {
    this.uhcTeamHandler.forcePlayerToTeam(leader, target);
  }

  @Command(names = "force-leave")
  public void forceLeave(
          final @Sender Player sender,
          final Player target
  ) {
    this.uhcTeamHandler.removePlayerOfTeam(target);
  }

  @Command(
          names = "force-create"
  )
  public void forceCreate(
          final @Sender Player sender,
          final Player target
  ) {
    this.uhcTeamManager.createTeam(target, null);
  }
}
