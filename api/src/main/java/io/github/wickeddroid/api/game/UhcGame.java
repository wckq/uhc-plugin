package io.github.wickeddroid.api.game;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface UhcGame {

  String getHost();

  void setHost(String host);

  UhcGameState getUhcGameState();

  void setUhcGameState(UhcGameState uhcGameState);

  long getStartTime();
  boolean loadedBackup();
  void setLoadedBackup(boolean loadedBackup);

  void setStartTime(long startTime);

  int getCurrentTime();

  void setCurrentTime(int currentTime);

  int getCurrentEpisodeTime();

  void setCurrentEpisodeTime(int currentEpisodeTime);

  int getWorldBorder();

  void setWorldBorder(int worldBorder);

  int getAppleRate();

  void setAppleRate(int appleRate);

  int getPlayersSize();

  void setPlayersSize(int playersSize);

  int getTimeForPvp();

  void setCobwebLimit(int cobwebLimit);

  int getCobwebLimit();

  void setTimeForPvp(int timeForPvp);

  int getTimeForMeetup();

  void setTimeForMeetup(int timeForMeetup);

  boolean isPvp();

  void setPvp(boolean pvp);

  boolean isGameStart();

  boolean isCleanItem();

  void setCleanItem(boolean cleanItem);

  boolean isTeamInventory();

  void setTeamInventory(boolean teamInventory);

  void setGameStart(boolean gameStart);

  boolean isTeamEnabled();

  void setTeamEnabled(boolean teamEnabled);

  boolean isOwnTeamsEnabled();
  void setOwnTeamsEnabled(boolean ownTeamsEnabled);

  int getTeamSize();

  void setTeamSize(int teamSize);
  List<String> getIronmans();
  List<String> getBackupPlayers();

  @Nullable String ironman();

  void setIronman(String ironman);

  @Nullable String paperman();

  void setPaperman(String paperman);
}
