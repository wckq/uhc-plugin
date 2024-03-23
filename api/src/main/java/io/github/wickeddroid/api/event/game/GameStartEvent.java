package io.github.wickeddroid.api.event.game;

import io.github.wickeddroid.api.event.UhcEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when the UHC Game is started.
 *
 * @since 1.0.0-BETA
 * @see org.bukkit.event.Event
 */
public class GameStartEvent extends UhcEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
