package io.github.wickeddroid.plugin.team;

import io.github.wickeddroid.api.team.UhcTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultUhcTeam implements UhcTeam, Serializable {

  private final List<String> members;
  private String leader;
  private Team team;
  private String name;
  private boolean alive;
  private int kills;
  private int playersAlive;
  private Inventory teamInventory;

  public DefaultUhcTeam(final String leader, final String name, final NamedTextColor color, final Component prefix, final boolean friendlyFire) {
    this.leader = leader;
    this.name = name;
    this.alive = true;
    this.kills = 0;
    this.playersAlive = 0;
    this.members = new ArrayList<>();
    this.teamInventory = Bukkit.createInventory(null, 27);

    if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(leader) == null) {
      setTeam(Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(leader));

      team.prefix(prefix);
      team.color(color);
      team.setAllowFriendlyFire(friendlyFire);
    } else {
      this.team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(leader);
    }

    this.addMember(leader);
  }

  @Override
  public void setTeam(Team team) {
    this.team = team;
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
    this.incrementPlayersAlive();
    this.team.addEntry(name);
  }

  @Override
  public void removeMember(String name) {
    this.members.remove(name);
    this.decrementPlayersAlive();
    this.team.removeEntry(name);
  }

  @Override
  public List<String> getMembers() {
    return members;
  }

  @Override
  public Team getTeam() {
    return this.team;
  }

  @Override
  public Inventory getTeamInventory() {
    return teamInventory;
  }
}
