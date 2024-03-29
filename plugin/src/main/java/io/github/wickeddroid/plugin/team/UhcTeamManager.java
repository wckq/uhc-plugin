package io.github.wickeddroid.plugin.team;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.InjectIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@InjectAll
public class UhcTeamManager {

  private UhcGame uhcGame;
  private Messages messages;
  private MessageHandler messageHandler;
  private UhcTeamHandler uhcTeamHandler;
  private UhcTeamRegistry uhcTeamRegistry;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private Teams teams;
  @InjectIgnore
  private Iterator<String> iterator;

  public void createTeam(
          final Player leader,
          String name
  ) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(leader.getName());

    if (!this.uhcGame.isTeamEnabled()) {
      this.messageHandler.send(leader, this.messages.team().teamHasNotEnabled());
      return;
    }

    if (this.getTeamByLeader(leader.getName()) != null) {
      this.messageHandler.send(leader, this.messages.team().alreadyExists());
      return;
    }

    if (name == null || name.isEmpty()) {
      name = String.format(teams.defaultName(), leader.getName());
    }

    this.uhcTeamRegistry.createTeam(
            leader.getName(),
            name,
            this.getColor(),
            this.replaceVariables(teams.teamPrefix(), leader),
            teams.friendlyFire()
    );

    this.messageHandler.send(leader, this.messages.team().create(), name);

    uhcPlayer.setUhcTeam(this.getTeamByLeader(leader.getName()));
  }


  public void removeTeam(final UUID leaderUUID) {
    var leaderOP = Bukkit.getOfflinePlayer(leaderUUID);

    if(leaderOP.getUniqueId() != leaderUUID) {
      return;
    }

    final var uhcTeam = this.getTeamByPlayer(leaderOP.getName());

    if (uhcTeam == null && leaderOP.isOnline()) {
      this.messageHandler.send(leaderOP.getPlayer(), this.messages.team().doesNotExist());
      return;
    }

    if (!uhcTeam.getLeader().equalsIgnoreCase(leaderOP.getName()) && leaderOP.isOnline()) {
      this.messageHandler.send(leaderOP.getPlayer(), this.messages.team().leaderAsMember());
      return;
    }

    for (final var member : uhcTeam.getMembers()) {
      final var player = Bukkit.getPlayer(member);

      if (player == null || !player.isOnline()) {
        continue;
      }

      this.messageHandler.send(player, this.messages.team().leave(), uhcTeam.getName());
      this.uhcPlayerRegistry.getPlayer(member).setUhcTeam(null);
    }

    if(leaderOP.isOnline()) {
      this.messageHandler.send(leaderOP.getPlayer(), this.messages.team().remove());

    }
    this.uhcTeamRegistry.removeTeam(leaderOP.getName());
  }

  public void randomizeTeams(
          final Player sender,
          final double teamSize
  ) {
    if (teamSize > 1) {
      final var playersWithoutTeam = Bukkit.getOnlinePlayers()
              .stream()
              .filter(player -> this.getTeamByPlayer(player.getName()) == null)
              .collect(Collectors.toCollection(ArrayList::new));

      Collections.shuffle(playersWithoutTeam);

      final var playersWithoutTeamSize = playersWithoutTeam.size();
      final var teamsNeeded = (int) Math.ceil(playersWithoutTeamSize / teamSize) + 1;

      for (int i = 0;i<teamsNeeded;i++) {
        final var leader = playersWithoutTeam.remove(0);

        this.createTeam(leader, null);

        final var uhcTeam = this.getTeamByPlayer(leader.getName());

        if (uhcTeam != null) {
          for (var j = 0;j<teamSize - 1;j++) {
            final var member = playersWithoutTeam.remove(0);

            this.uhcTeamHandler.addPlayerToTeam(leader, member, true);
          }
        }
      }
    } else {
      Bukkit.getOnlinePlayers().forEach(p -> createTeam(p, null));
    }
  }

  public void sendMessageTeam(
          final Player player,
          final Component message
  ) {
    final var uhcTeam = this.getTeamByPlayer(player.getName());

    if (uhcTeam == null) {
      return;
    }

    for (final var memberName : uhcTeam.getMembers()) {
      final var member = Bukkit.getPlayer(memberName);

      if (member == null || !member.isOnline()) {
        return;
      }

      member.sendMessage(MessageUtil.parseStringToComponent(
              "<red>[Team] <white><player></white> <red>➣</red> ",
              Placeholder.parsed("player", player.getName())
      ).append(message));
    }
  }

  public UhcTeam getTeamByLeader(final String leader) {
    return this.uhcTeamRegistry.getTeam(leader);
  }

  public UhcTeam getTeamByPlayer(final String player) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player);

    return uhcPlayer == null ? null : uhcPlayer.getUhcTeam();
  }

  public Component replaceVariables(
          final String text,
          final Player leader
  ) {

    return MessageUtil.parseStringToComponent(
            text,
            Placeholder.parsed("leader", leader.getName())
    );
  }

  private NamedTextColor getColor() {
    if(!teams.forceSequentiallyColors()) {
      return NamedTextColor.NAMES.valueOr(teams.colors().get(ThreadLocalRandom.current().nextInt(teams.colors().size())), NamedTextColor.WHITE);
    }

    if(this.iterator == null) {
      this.iterator = teams.colors().iterator();
    }

    if(!this.iterator.hasNext()) {
      this.iterator = teams.colors().iterator();
    }

    return NamedTextColor.NAMES.valueOr(iterator.next(), NamedTextColor.WHITE);
  }
}
