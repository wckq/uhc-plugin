package io.github.wickeddroid.plugin.team;

import io.github.wickeddroid.api.team.UhcTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultUhcTeam implements UhcTeam {
  private final List<String> members;
  private String leader;
  private String name;
  private boolean alive;
  private int kills;
  private int playersAlive;

  public DefaultUhcTeam(
          final String leader,
          final String name
  ) {
    this.leader = leader;
    this.name = name;
    this.alive = true;
    this.kills = 0;
    this.playersAlive = 1;
    this.members = new ArrayList<>();
    this.members.add(leader);
  }

  @Override
  public void setLeader(String leader) {
    this.leader = leader;
  }

  @Override
  public String getLeader() {
    return leader;
  }

  @Override
  public void setKills(int kills) {
    this.kills = kills;
  }

  @Override
  public void incrementKills() {
    ++this.kills;
  }

  @Override
  public void decrementKills() {
    --this.kills;
  }

  @Override
  public int getKills() {
    return kills;
  }

  @Override
  public void setPlayersAlive(int playersAlive) {
    this.playersAlive = playersAlive;
  }

  @Override
  public void incrementPlayersAlive() {
    ++this.playersAlive;
  }

  @Override
  public void decrementPlayersAlive() {
    --this.playersAlive;
  }

  @Override
  public int getPlayersAlive() {
    return playersAlive;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setAlive(boolean alive) {
    this.alive = alive;
  }

  @Override
  public boolean isAlive() {
    return alive;
  }

  @Override
  public void addMember(String name) {
    this.members.add(name);
  }

  @Override
  public void removeMember(String name) {
    this.members.remove(name);
  }

  @Override
  public List<String> getMembers() {
    return members;
  }
}
