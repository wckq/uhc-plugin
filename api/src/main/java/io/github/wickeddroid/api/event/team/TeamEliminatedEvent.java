package io.github.wickeddroid.api.event.team;

import io.github.wickeddroid.api.event.UhcEvent;
import io.github.wickeddroid.api.team.UhcTeam;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamEliminatedEvent extends UhcEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final UhcTeam uhcTeam;

    public TeamEliminatedEvent(@NotNull UhcTeam uhcTeam) {
        this.uhcTeam = uhcTeam;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }
}
