package io.github.wickeddroid.plugin.event.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerScatteredEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final Location location;
  private final int scattered;

  public PlayerScatteredEvent(
          final Player player,
          final Location location,
          final int scattered
  ) {
    this.player = player;
    this.location = location;
    this.scattered = scattered;
  }

  public Player getPlayer() {
    return player;
  }

  public Location getLocation() {
    return location;
  }

  public int getScattered() {
    return scattered;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
