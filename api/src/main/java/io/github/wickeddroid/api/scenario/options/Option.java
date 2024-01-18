package io.github.wickeddroid.api.scenario.options;

import com.google.common.collect.Maps;
import io.github.wickeddroid.api.events.ScenarioOptionValueChangeEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Option<T> {
    @NotNull
    private final String optionName;
    @NotNull
    private OptionValue<T> value;
    @NotNull
    private final LinkedHashMap<Object, OptionValue<T>> optionsMap;

    private Option(@NotNull String optionName, @NotNull T defaultValue, @NotNull LinkedList<OptionValue<T>> options) {
        this.optionName = optionName;
        this.value = new OptionValue<>(defaultValue);
        this.optionsMap = options.stream().collect(Maps::newLinkedHashMap, (o, v) -> o.put(v.get(), v), (l, r)->{});

        if(optionsMap.containsKey(value.get())) {
            this.value = optionsMap.get(value);
        }
    }

    public String optionName() { return  optionName; }

    public OptionValue<T> value() { return value; }

    public LinkedList<OptionValue<T>> options() {
        return new LinkedList<>(optionsMap.values());
    }

    public void setValue(OptionValue<T> value) {
        if(!this.options().contains(value)) {
            throw new IllegalArgumentException("This option value is not part of the scenario option");
        }

        var event = new ScenarioOptionValueChangeEvent<T>(this, value);
        Bukkit.getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            this.value = value;
        }
    }

    public static <T> OptionValue<T> buildValue(T value, String valueDisplay) {
        return new OptionValue<>(value, valueDisplay);
    }

    public static <T> OptionValue<T> buildValue(T value) {
        return new OptionValue<>(value);
    }

    public static <T> Option<T> createOption(@NotNull String optionName, @NotNull T defaultValue, @NotNull List<OptionValue<T>> options) {
        LinkedList<OptionValue<T>> list = new LinkedList<>(options);
        return new Option<>(optionName, defaultValue, list);
    }


    public static <T> List<OptionValue<T>> createValues(
            T v1, String d1,
            T v2, String d2,
            T v3, String d3,
            T v4, String d4,
            T v5, String d5
    ) {
        return List.of(
                buildValue(v1, d1),
                buildValue(v2, d2),
                buildValue(v3, d3),
                buildValue(v4, d4),
                buildValue(v5, d5)
        );
    }
}
