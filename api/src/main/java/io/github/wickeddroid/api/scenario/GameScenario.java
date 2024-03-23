package io.github.wickeddroid.api.scenario;

import io.github.wickeddroid.api.exception.NotEnabledFeatureException;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.scenario.options.ScenarioOption;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Material;

import java.util.*;

public class GameScenario {

  private final String name;
  private final String key;
  private final String[] description;
  private final Material material;
  private final boolean experimental;
  private boolean enabled;
  private final boolean supportsOptions;
  private HashMap<String, Option<?>> options;
  private Map<String, String> dynamicValues;

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

      if (supportsOptions) {
        this.options = new HashMap<>();
        this.dynamicValues = new HashMap<>();
      }
    } else if (this.getClass().isAnnotationPresent(Setting.class)) {
      final var annotation = this.getClass().getAnnotation(Setting.class);

      this.name = annotation.name();
      this.key = annotation.key();
      this.description = annotation.description();
      this.material = annotation.material();
      this.enabled = false;
      this.supportsOptions = false;
      this.experimental = false;
    } else {
      throw new IllegalArgumentException("An error has ocurred with the following Scenario: " + this.getClass().getSimpleName());
    }
  }

  protected void addOptions(Option<?>... options) {
    for (var option : options) {
      if (!supportsOptions) {
        throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
      }
      this.options.put(option.optionName(), option);
    }
  }

  protected void addOption(Option<?> option) {
    if (!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    this.options.put(option.optionName(), option);
  }


  public HashMap<String, Option<?>> getOptions() {
    if (!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    return options;
  }

  public <T> T getOptionValue(String name) {
    if (!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    var option = options.get(name);

    if (option == null) {
      throw new NullPointerException("Not valid option");
    }

    return (T) option.value();
  }

  public Option<?> getOption(String name) {
    if (!supportsOptions) {
      throw new NotEnabledFeatureException("Options are not enabled on this scenario.");
    }

    return options.get(name);
  }

  public void createOptions() {
      Arrays.stream(FieldUtils.getFieldsWithAnnotation(this.getClass(), ScenarioOption.class)).forEach(f -> {
        try {
          f.setAccessible(true);

          var annotation = f.getAnnotation(ScenarioOption.class);

          var linkedList = (LinkedList<OptionValue<Object>>) f.get(this);

          var optName = annotation.optionName();
          var dynamic = annotation.dynamicValue();

          if (!dynamic.equals("")) {
            var field = this.getClass().getDeclaredField(dynamic);
            field.setAccessible(true);

            field.set(this, linkedList.get(0).get());
            dynamicValues.put(optName, dynamic);
          }

          this.addOption(Option.createOption(
                  optName,
                  linkedList.get(0).get(),
                  linkedList
          ));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
  }

  public void changeOptionValue(String option, Object o) {
    if(dynamicValues.containsKey(option)) {
      try {
        var dynamic = dynamicValues.get(option);

        var field = this.getClass().getDeclaredField(dynamic);
        field.setAccessible(true);

        field.set(this, o);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
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

  public boolean isSupportsOptions() {
    return supportsOptions;
  }
}
