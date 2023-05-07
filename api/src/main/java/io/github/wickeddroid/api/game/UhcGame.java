package io.github.wickeddroid.api.game;

public interface UhcGame {
  String getHost();

  void setHost(String host);

  UhcGameState getUhcGameState();

  void setUhcGameState(UhcGameState uhcGameState);

  long getStartTime();

  void setStartTime(long startTime);

  int getCurrentTime();

  void setCurrentTime(int currentTime);

  int getWorldBorder();

  void setWorldBorder(int worldBorder);

  int getAppleRate();

  void setAppleRate(int appleRate);

  int getTimeForPvp();

  void setTimeForPvp(int timeForPvp);

  int getTimeForMeetup();

  void setTimeForMeetup(int timeForMeetup);

  boolean isPvp();

  void setPvp(boolean pvp);

  boolean isGameStart();

  void setGameStart(boolean gameStart);
}
