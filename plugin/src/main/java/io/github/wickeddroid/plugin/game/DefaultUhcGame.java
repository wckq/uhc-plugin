package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.unnamed.inject.Singleton;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DefaultUhcGame implements UhcGame {

  private String host;
  private UhcGameState uhcGameState;
  private long startTime;
  private int currentTime;
  private int currentEpisodeTime;
  private int cobwebLimit;
  private int teamSize;
  private int worldBorder;
  private int appleRate;
  private int playersSize;
  private int timeForPvp;
  private int timeForMeetup;
  private boolean cleanItem;
  private boolean teamInventory;
  private boolean pvp;
  private boolean gameStart;
  private boolean teamEnabled;
  private boolean ownTeamsEnabled;
  private boolean loadedBackup;
  private List<String> ironmans;
  private List<String> backupPlayers;

  private @Nullable String ironman, paperman;

  public DefaultUhcGame(final String host) {
    this.host = host;
    this.uhcGameState = UhcGameState.WAITING;
    this.startTime = 0;
    this.currentTime = 0;
    this.currentEpisodeTime = 0;
    this.appleRate = -1;
    this.playersSize = Bukkit.getMaxPlayers();
    this.cobwebLimit = 64;
    this.timeForPvp = 3600;
    this.timeForMeetup = 7200;
    this.teamSize = 2;
    this.worldBorder = 2000;
    this.pvp = false;
    this.cleanItem = false;
    this.teamInventory = false;
    this.gameStart = false;
    this.teamEnabled = false;
    this.ownTeamsEnabled = false;
    this.loadedBackup = false;
    this.ironmans = new ArrayList<>();
    this.backupPlayers = new ArrayList<>();
  }

  public DefaultUhcGame() {
    this(null);
  }

  @Override
  public boolean loadedBackup() {
    return loadedBackup;
  }

  @Override
  public void setLoadedBackup(boolean loadedBackup) {
    this.loadedBackup = loadedBackup;
  }

  @Override
  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public String getHost() {
    return this.host;
  }

  @Override
  public UhcGameState getUhcGameState() {
    return this.uhcGameState;
  }

  @Override
  public void setUhcGameState(UhcGameState uhcGameState) {
    this.uhcGameState = uhcGameState;
  }

  @Override
  public long getStartTime() {
    return this.startTime;
  }

  @Override
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @Override
  public int getCurrentTime() {
    return this.currentTime;
  }

  @Override
  public int getCurrentEpisodeTime() {
    return currentEpisodeTime;
  }

  @Override
  public void setCurrentEpisodeTime(int currentEpisodeTime) {
    this.currentEpisodeTime = currentEpisodeTime;
  }

  @Override
  public void setCurrentTime(int currentTime) {
    this.currentTime = currentTime;
  }

  @Override
  public int getWorldBorder() {
    return this.worldBorder;
  }

  @Override
  public void setAppleRate(int appleRate) {
    this.appleRate = appleRate;
  }

  @Override
  public void setTeamInventory(boolean teamInventory) {
    this.teamInventory = teamInventory;
  }

  @Override
  public boolean isTeamInventory() {
    return teamInventory;
  }

  @Override
  public int getAppleRate() {
    return this.appleRate;
  }

  @Override
  public int getPlayersSize() {
    return playersSize;
  }

  @Override
  public void setPlayersSize(int playersSize) {
    this.playersSize = playersSize;
  }

  @Override
  public void setTimeForPvp(int timeForPvp) {
    this.timeForPvp = timeForPvp;
  }

  @Override
  public int getTimeForPvp() {
    return this.timeForPvp;
  }

  @Override
  public void setCobwebLimit(int cobwebLimit) {
    this.cobwebLimit = cobwebLimit;
  }

  @Override
  public int getCobwebLimit() {
    return 0;
  }

  @Override
  public void setTimeForMeetup(int timeForMeetup) {
    this.timeForMeetup = timeForMeetup;
  }

  @Override
  public int getTimeForMeetup() {
    return this.timeForMeetup;
  }

  @Override
  public void setWorldBorder(int worldBorder) {
    this.worldBorder = worldBorder;
  }

  @Override
  public boolean isPvp() {
    return this.pvp;
  }

  @Override
  public void setPvp(boolean pvp) {
    this.pvp = pvp;
  }

  @Override
  public boolean isGameStart() {
    return this.gameStart;
  }

  @Override
  public void setCleanItem(boolean cleanItem) {
      this.cleanItem = cleanItem;
  }

  @Override
  public boolean isCleanItem() {
    return cleanItem;
  }

  @Override
  public void setGameStart(boolean gameStart) {
    this.gameStart = gameStart;
  }

  @Override
  public boolean isTeamEnabled() {
    return this.teamEnabled;
  }

  @Override
  public boolean isOwnTeamsEnabled() {
    return this.ownTeamsEnabled;
  }

  @Override
  public void setOwnTeamsEnabled(boolean ownTeamsEnabled) {
    this.ownTeamsEnabled = ownTeamsEnabled;
  }

  @Override
  public void setTeamEnabled(boolean teamEnabled) {
    this.teamEnabled = teamEnabled;
  }

  @Override
  public int getTeamSize() {
    return this.teamSize;
  }

  @Override
  public void setTeamSize(int teamSize) {
    this.teamSize = teamSize;
  }

  @Override
  public List<String> getIronmans() {
    return ironmans;
  }

  @Override
  public List<String> getBackupPlayers() {
    return backupPlayers;
  }

  @Nullable
  @Override
  public String ironman() {
    return ironman;
  }

  @Override
  public void setIronman(@NotNull String ironman) {
    this.ironman = ironman;
  }

  @Nullable
  @Override
  public String paperman() {
    return paperman;
  }

  @Override
  public void setPaperman(@NotNull String paperman) {
    this.paperman = paperman;
  }
}
