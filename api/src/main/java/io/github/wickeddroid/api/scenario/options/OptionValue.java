package io.github.wickeddroid.api.scenario.options;

import java.util.List;

public class OptionValue<T> {

    private final T value;

    protected OptionValue(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }
}
