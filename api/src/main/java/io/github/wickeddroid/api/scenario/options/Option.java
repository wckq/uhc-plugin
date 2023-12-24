package io.github.wickeddroid.api.scenario.options;

import io.github.wickeddroid.api.events.ScenarioOptionValueChangeEvent;
import io.github.wickeddroid.api.scenario.GameScenario;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Option<T> {
    @NotNull
    private final String optionName;
    @NotNull
    private OptionValue<T> value;
    @NotNull
    private final LinkedList<OptionValue<T>> options;

    private Option(@NotNull String optionName, @NotNull T defaultValue, @NotNull LinkedList<OptionValue<T>> options) {
        this.optionName = optionName;
        this.value = new OptionValue<>(defaultValue);
        this.options = options;

    }

    private Option(@NotNull String optionName, @NotNull T defaultValue) {
        this(optionName, defaultValue, new LinkedList<>());
    }


    public String optionName() { return  optionName; }

    public OptionValue<T> value() { return value; }

    public LinkedList<OptionValue<T>> options() {
        return options;
    }

    public void setValue(OptionValue<T> value) {
        if(!options.contains(value)) {
            throw new IllegalArgumentException("This option value is not part of the scenario option");
        }

        var event = new ScenarioOptionValueChangeEvent<T>(this, value);
        Bukkit.getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            this.value = value;
        }
    }

    @SafeVarargs
    public final void addOptionValues(OptionValue<T>... optionValues) {
        options.addAll(List.of(optionValues));
    }


    public OptionValue<T> buildValue(T value) {
        return new OptionValue<>(value);
    }

    public static <T> Option<T> createOption(@NotNull String optionName, @NotNull T defaultValue) {
        return new Option<>(optionName, defaultValue);
    }

    public static <T> Option<T> createOption(@NotNull String optionName, @NotNull T defaultValue, @NotNull List<OptionValue<T>> options) {
        LinkedList<OptionValue<T>> list = new LinkedList<>(options);
        return new Option<>(optionName, defaultValue, list);
    }
}
