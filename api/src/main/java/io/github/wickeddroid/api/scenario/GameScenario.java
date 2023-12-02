package io.github.wickeddroid.api.scenario;

import io.github.wickeddroid.api.exception.NotEnabledFeatureException;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class GameScenario {

  private final String name;
  private final String key;
  private final String[] description;
  private final Material material;
  private final boolean experimental;
  private boolean enabled, supportsOptions;
  private HashMap<String, Option<?>> options;

  public GameScenario() {
    if (this.getClass().isAnnotationPresent(Scenario.class)) {
      final var annotation = this.getClass().getAnnotation(Scenario.class);

      this.name = annotation.name();
      this.key = annotation.key();
      this.description = annotation.description();
      this.material = annotation.material();
      this.experimental = annotation.experimental();
      this.enabled = false;
      this.supportsOptions = annotation.supportsOptions();

      if(supportsOptions) {
        this.options = new HashMap<>();
      }
    } else {
      throw new IllegalArgumentException("An error has ocurred with the following Scenario: " + this.getClass().getSimpleName());
    }
  }

  public void addOption(Option<?> option) {
    if(!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }
    options.put(option.optionName(), option);
  }

  public HashMap<String, Option<?>> getOptions() {
    if(!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    return options;
  }

  public <T> T getOptionValue(String name) {
    if(!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    var option = options.get(name);

    if(option == null) {
      throw new NullPointerException("Not valid option");
    }

    return (T) option.value();
  }

  public Option<?> getOption(String name) {
    if(!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    return options.get(name);
  }

  public String getName() {
    return this.name;
  }

  public String getKey() {
    return this.key;
  }

  public String[] getDescription() {
    return this.description;
  }

  public Material getMaterial() {
    return this.material;
  }

  public boolean isExperimental() {
    return this.experimental;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}
