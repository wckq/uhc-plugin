package io.github.wickeddroid.api.scenario.options;

import com.google.common.collect.Maps;
import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.event.scenario.ScenarioOptionValueChangeEvent;
import io.github.wickeddroid.api.scenario.GameScenario;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
            this.value = optionsMap.get(value.get());
        }
    }

    public String optionName() { return  optionName; }

    public OptionValue<T> value() { return value; }

    public LinkedList<OptionValue<T>> options() {
        return new LinkedList<>(optionsMap.values());
    }

    public void setValue(GameScenario scenario, OptionValue<T> value) {
        if(!this.options().contains(value)) {
            throw new IllegalArgumentException("This option value is not part of the scenario option");
        }

        var event = UhcEventManager.fireScenarioOptionValueChange(this, value);

        if(!event.isCancelled()) {
            this.value = value;
            scenario.changeOptionValue(optionName, value.get());
        }
    }

    public static <T> OptionValue<T> buildValue(T value, String valueDisplay) {
        return new OptionValue<>(value, valueDisplay);
    }

    public static LinkedList<OptionValue<Boolean>> buildBooleanValues(boolean defaultValue) {
        return new LinkedList<>(
                List.of(
                        Option.buildValue(defaultValue, defaultValue ? "Habilitado" : "Deshabilitado"),
                        Option.buildValue(!defaultValue, !defaultValue ? "Habilitado" : "Deshabilitado")
                )
        );
    }
    public static LinkedList<OptionValue<Integer>> buildRangedValues(int min, int max, String valueDisplay) {
        var l = new LinkedList<OptionValue<Integer>>();

        if(min < 0) {
            throw new IndexOutOfBoundsException();
        }

        for(int i = min; i < max; i++) {
            l.add(
                    buildValue(i, i + " " + valueDisplay)
            );
        }

        return l;
    }

    public static LinkedList<OptionValue<Integer>> buildRangedValues(int min, int max, int range, String valueDisplay) {
        var l = new LinkedList<OptionValue<Integer>>();

        if(min < 0 || max % range != 0) {
            throw new IndexOutOfBoundsException();
        }

        for(int i = min; i < max; i+=range) {
            l.add(
                    buildValue(i, i + " " + valueDisplay)
            );
        }

        return l;
    }

    public static <T> OptionValue<T> buildValue(T value) {
        return new OptionValue<>(value);
    }

    public static <T> Option<T> createOption(@NotNull String optionName, @NotNull T defaultValue, @NotNull LinkedList<OptionValue<T>> options) {
        LinkedList<OptionValue<T>> list = new LinkedList<>(options);
        return new Option<>(optionName, defaultValue, list);
    }


    public static <T> LinkedList<OptionValue<T>> createValues(
            T v1, String d1,
            T v2, String d2,
            T v3, String d3,
            T v4, String d4,
            T v5, String d5,
            T v6, String d6
    ) {
        return new LinkedList<>(List.of(
                buildValue(v1, d1),
                buildValue(v2, d2),
                buildValue(v3, d3),
                buildValue(v4, d4),
                buildValue(v5, d5),
                buildValue(v6, d6)
        ));
    }
}
