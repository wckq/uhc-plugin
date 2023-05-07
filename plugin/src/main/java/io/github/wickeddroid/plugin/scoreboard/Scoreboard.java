package io.github.wickeddroid.plugin.scoreboard;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class Scoreboard {
  private String title = "<rainbow>UHC";
  private Lobby lobby = new Lobby();

  public @NonNull String title() {
    return title;
  }

  public @NonNull Lobby lobby() {
    return this.lobby;
  }

  @ConfigSerializable
  public static class Lobby {
    private List<String> lines = new ArrayList<>();

    public @NonNull List<String> lines() {
      return lines;
    }
  }
}
