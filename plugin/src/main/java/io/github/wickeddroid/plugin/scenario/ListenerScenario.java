package io.github.wickeddroid.plugin.scenario;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public class ListenerScenario implements GameScenario, Listener {
  private final String name;
  private final String key;
  private final String[] description;
  private final Material material;
  private final boolean experimental;

  public ListenerScenario() {
    if (this.getClass().isAnnotationPresent(Scenario.class)) {
      final var annotation = this.getClass().getAnnotation(Scenario.class);

      this.name = annotation.name();
      this.key = annotation.key();
      this.description = annotation.description();
      this.material = annotation.material();
      this.experimental = annotation.experimental();
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
}
