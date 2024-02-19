package io.github.wickeddroid.api.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a Player is Scattered (Teleported).
 *
 * @since 1.2.0-BETA
 * @see org.bukkit.event.Event
 * @see io.github.wickeddroid.api.team.ScatterTask
 */
public class PlayerScatteredEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final Location location;
  private final boolean laterScatter;

  /**
   *
   * @param player The {@link Player} that is scattered.
   * @param location The {@link Location} where the player is scattered.
   * @param laterScatter If player is scattered after the game is started.
   *
   * @since 1.2.0-BETA
   */
  public PlayerScatteredEvent(
          final Player player,
          final Location location,
          final boolean laterScatter
  ) {
    this.player = player;
    this.location = location;
    this.laterScatter = laterScatter;
  }

  public Player getPlayer() {
    return player;
  }

  public Location getLocation() {
    return location;
  }

  public boolean isLaterScatter() {
    return laterScatter;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
