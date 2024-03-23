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
    private String teamDisplay = "  » <player-name> <player-hp><status-symbol>";
    private int maxTeamDisplay = 1;
    private boolean displayDeadPlayers = true;
    private Symbols symbols = new Symbols();

    private List<String> lines = new ArrayList<>();

    public @NonNull Symbols symbols() { return this.symbols; }
    public @NonNull List<String> lines() {
      return lines;
    }
    public @NonNull String teamDisplay() { return this.teamDisplay; }
    public int maxTeamDisplay() { return this.maxTeamDisplay; }
    public boolean displayDeadPlayers() { return this.displayDeadPlayers; }

    @ConfigSerializable
    public static class Symbols {
      private String alive = "❤";
      private String dead = "\uD83D\uDC80";
      private String disconnected = "\uD83D\uDC80";

      public @NonNull String alive() { return this.alive; }
      public @NonNull String dead() { return this.dead; }
      public @NonNull String disconnected() { return this.disconnected; }
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
