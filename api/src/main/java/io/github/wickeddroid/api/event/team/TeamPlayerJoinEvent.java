package io.github.wickeddroid.api.event.team;

import io.github.wickeddroid.api.event.UhcEvent;
import io.github.wickeddroid.api.team.UhcTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamPlayerJoinEvent extends UhcEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }


    private final Player player;
    private final UhcTeam uhcTeam;

    public TeamPlayerJoinEvent(@NotNull Player player, @NotNull UhcTeam uhcTeam) {
        this.player = player;
        this.uhcTeam = uhcTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }
}
