package io.github.wickeddroid.plugin.scenario;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public abstract class ListenerScenario implements GameScenario, Listener {

  private final String name;
  private final String key;
  private final String[] description;
  private final Material material;
  private final boolean experimental;
  private boolean enabled;

  public ListenerScenario() {
    if (this.getClass().isAnnotationPresent(Scenario.class)) {
      final var annotation = this.getClass().getAnnotation(Scenario.class);

      this.name = annotation.name();
      this.key = annotation.key();
      this.description = annotation.description();
      this.material = annotation.material();
      this.experimental = annotation.experimental();
      this.enabled = false;
    } else {
      throw new IllegalArgumentException("Has error ocurred with Scenario " + this.getClass().getSimpleName());
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Override
  public String[] getDescription() {
    return this.description;
  }

  @Override
  public Material getMaterial() {
    return this.material;
  }

  @Override
  public boolean isExperimental() {
    return this.experimental;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
