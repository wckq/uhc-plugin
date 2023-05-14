package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;

public class DefaultUhcGame implements UhcGame {
  private String host;
  private UhcGameState uhcGameState;
  private long startTime;
  private int currentTime;
  private int teamSize;
  private int worldBorder;
  private int appleRate;
  private int timeForPvp;
  private int timeForMeetup;
  private int playersDeathForPve;
  private int playersDeathForPvp;
  private boolean pvp;
  private boolean gameStart;
  private boolean teamEnabled;

  public DefaultUhcGame(final String host) {
    this.host = host;
    this.uhcGameState = UhcGameState.WAITING;
    this.startTime = 0;
    this.currentTime = 0;
    this.appleRate = 1;
    this.timeForPvp = 3600;
    this.timeForMeetup = 7200;
    this.teamSize = 2;
    this.worldBorder = 2000;
    this.playersDeathForPve = 0;
    this.playersDeathForPvp = 0;
    this.pvp = false;
    this.gameStart = false;
    this.teamEnabled = false;
  }

  public DefaultUhcGame() {
    this(null);
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
  public int getAppleRate() {
    return this.appleRate;
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
  public void setGameStart(boolean gameStart) {
    this.gameStart = gameStart;
  }

  @Override
  public boolean isTeamEnabled() {
    return this.teamEnabled;
  }

  @Override
  public void setTeamEnabled(boolean teamEnabled) {
    this.teamEnabled = teamEnabled;
  }

  @Override
  public int getPlayersDeathForPvE() {
    return this.playersDeathForPve;
  }

  @Override
  public void incrementPlayersDeathForPve() {
    ++this.playersDeathForPve;
  }

  @Override
  public void setPlayerDeathForPve(int playersDeathForPve) {
    this.playersDeathForPve = playersDeathForPve;
  }


  @Override
  public int getPlayersDeathForPvP() {
    return this.playersDeathForPvp;
  }

  @Override
  public void incrementPlayersDeathForPvp() {
    ++this.playersDeathForPvp;
  }

  @Override
  public void setPlayersDeathForPvp(int playersDeathForPvp) {
    this.playersDeathForPvp = playersDeathForPvp;
  }

  @Override
  public int getTeamSize() {
    return this.teamSize;
  }

  @Override
  public void setTeamSize(int teamSize) {
    this.teamSize = teamSize;
  }
}
