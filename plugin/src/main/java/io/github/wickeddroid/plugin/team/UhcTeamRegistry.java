package io.github.wickeddroid.plugin.team;

import io.github.wickeddroid.api.team.UhcTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import team.unnamed.inject.Singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class UhcTeamRegistry {

  private final Map<String, UhcTeam> teamMap = new HashMap<>();

  public void createTeam(
          final String leader,
          final String name,
          final NamedTextColor color,
          final Component prefix,
          final boolean friendlyFire
          ) {
    this.teamMap.put(leader, new DefaultUhcTeam(
            leader, name, color, prefix, friendlyFire
    ));
  }

  public void removeTeam(final String leader) {
    final var team = this.teamMap.get(leader);

    team.getTeam().unregister();
    this.teamMap.remove(leader);
  }

  public Map<String, UhcTeam> getTeamMap() {
    return teamMap;
  }

  public UhcTeam getTeamByLeader(final String leader) {
    return this.teamMap.get(leader);
  }

  public void setBackupTeams(Map<String, UhcTeam> backupTeams) {
    this.teamMap.putAll(backupTeams);
  }

  public Collection<UhcTeam> getTeams() {
    return this.teamMap.values();
  }
}
