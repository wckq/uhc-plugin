package io.github.wickeddroid.plugin.scoreboard;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class Scoreboard {

  private String title = "<rainbow>UHC";
  private Lobby lobby = new Lobby();
  private Game game = new Game();
  private End end = new End();

  public @NonNull String title() {
    return title;
  }

  public @NonNull Lobby lobby() {
    return this.lobby;
  }

  public @NonNull Game game() {
    return this.game;
  }

  public @NonNull End end() {
    return this.end;
  }

  @ConfigSerializable
  public static class Lobby {
    private List<String> lines = new ArrayList<>();

    public @NonNull List<String> lines() {
      return lines;
    }
  }

  @ConfigSerializable
  public static class Game {
    private List<String> lines = new ArrayList<>();

    public @NonNull List<String> lines() {
      return lines;
    }
  }

  @ConfigSerializable
  public static class End {
    private List<String> lines = new ArrayList<>();

    public @NonNull List<String> lines() {
      return lines;
    }
  }
}
