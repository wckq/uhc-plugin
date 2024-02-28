package io.github.wickeddroid.api.event.scenario;

import io.github.wickeddroid.api.event.UhcEvent;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *  This event is called when a Scenario Option changes its value.
 *
 * @param <T> {@link java.lang.reflect.Type} of Option return value.
 *
 * @since 1.0.0-BETA
 * @author Agus5534
 * @see org.bukkit.event.Event
 * @see Option
 */
public class ScenarioOptionValueChangeEvent<T> extends UhcEvent implements Cancellable {

    private boolean cancelled = false;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }


    private final @NotNull Option<T> option;
    private final @NotNull OptionValue<T> optionValue;

    /**
     *
     * @param option
     * @param optionValue
     */
    public ScenarioOptionValueChangeEvent(@NotNull Option<T> option, @NotNull OptionValue<T> optionValue) {
        this.option = option;
        this.optionValue = optionValue;
    }

    public OptionValue<T> optionValue() {
        return optionValue;
    }

    public Option<T> option() {
        return option;
    }

}
