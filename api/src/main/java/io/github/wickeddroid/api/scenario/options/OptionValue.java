package io.github.wickeddroid.api.scenario.options;

import org.bukkit.Material;

public class OptionValue<T> {

    private final T value;
    private final String valueDisplay;

    public OptionValue(T value) {
        this.value = value;
        this.valueDisplay = value.toString();
    }

    public OptionValue(T value, String valueDisplay) {
        this.value = value;
        this.valueDisplay = valueDisplay;
    }

    public String getAsString() {
        return (String)value;
    }

    public Double getAsDouble() {
        return (Double)value;
    }

    public Integer getAsInteger() {
        return (Integer)value;
    }

    public Float getAsFloat() {
        return (Float)value;
    }

    public Boolean getAsBoolean() { return (Boolean) value; }

    public Object get() {
        return value;
    }

    public Material getAsMaterial() { return (Material) value; }

    public String getValueDisplay() {
        return valueDisplay;
    }
}
