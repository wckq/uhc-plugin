package io.github.wickeddroid.plugin.team;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

@InjectAll
public class UhcTeamManager {

  private UhcGame uhcGame;
  private Messages messages;
  private MessageHandler messageHandler;
  private UhcTeamHandler uhcTeamHandler;
  private UhcTeamRegistry uhcTeamRegistry;
  private UhcPlayerRegistry uhcPlayerRegistry;

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
      name = String.format("%s team", leader.getName());
    }

    this.uhcTeamRegistry.createTeam(
            leader.getName(),
            name
    );

    this.messageHandler.send(leader, this.messages.team().create(), name);

    uhcPlayer.setUhcTeam(this.getTeamByLeader(leader.getName()));
  }

  public void removeTeam(final Player leader) {
    final var uhcTeam = this.getTeamByPlayer(leader.getName());

    if (uhcTeam == null) {
      this.messageHandler.send(leader, this.messages.team().doesNotExist());
      return;
    }

    if (!uhcTeam.getLeader().equalsIgnoreCase(leader.getName())) {
      this.messageHandler.send(leader, this.messages.team().leaderAsMember());
      return;
    }

    for (final var member : uhcTeam.getMembers()) {
      final var player = Bukkit.getPlayer(member);

      if (player == null || !player.isOnline()) {
        return;
      }

      this.messageHandler.send(player, this.messages.team().leave(), uhcTeam.getName());
      this.uhcPlayerRegistry.getPlayer(member).setUhcTeam(null);
    }

    this.messageHandler.send(leader, this.messages.team().remove());
    this.uhcTeamRegistry.removeTeam(leader.getName());
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
              "<red>[Team] <white><player> <red>➣ <white>",
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
}
