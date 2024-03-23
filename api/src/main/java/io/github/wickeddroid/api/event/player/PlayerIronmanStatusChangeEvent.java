package io.github.wickeddroid.api.event.player;

import io.github.wickeddroid.api.event.UhcEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerIronmanStatusChangeEvent extends UhcEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final Status currentStatus;
    private final Status oldStatus;
    private final Player player;

    public PlayerIronmanStatusChangeEvent(@NotNull Player player, @NotNull Status currentStatus, @NotNull Status oldStatus) {
        this.player = player;
        this.currentStatus = currentStatus;
        this.oldStatus = oldStatus;
    }

    public enum Status {
        WINNER,
        LOST,
        PAPERMAN,
        COMPETITOR
    }

    public Player getPlayer() {
        return player;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public Status getOldStatus() {
        return oldStatus;
    }
}
