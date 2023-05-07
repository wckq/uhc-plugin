package io.github.wickeddroid.plugin.event.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameTickEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final int time;
  private final int taskId;

  public GameTickEvent(
          final int time,
          final int taskId,
          final boolean async
  ) {
    super(async);
    this.time = time;
    this.taskId = taskId;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public int getTime() {
    return this.time;
  }

  public int getTaskId() {
    return this.taskId;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
