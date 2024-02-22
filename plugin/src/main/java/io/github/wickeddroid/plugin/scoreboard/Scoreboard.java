package io.github.wickeddroid.plugin.scoreboard;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class Scoreboard {

  private String title = "<rainbow>UHC";
  @Comment("Lobby Scoreboard. Used when game is not started")
  private Lobby lobby = new Lobby();
  @Comment("Game Scoreboard. Used when game is started")
  private Game game = new Game();
  @Comment("End Scoreboard. Used when game has finished")
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
    @Comment("Display of tag <team-members> (if used).")
    private String teamDisplay = "  » <player-name> <player-hp><status-symbol>";
    @Comment("Max players that are displayed on scoreboard. Must be greater than or equal to 1.")
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
