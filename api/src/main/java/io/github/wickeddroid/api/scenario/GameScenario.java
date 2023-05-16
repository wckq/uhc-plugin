package io.github.wickeddroid.api.scenario;

import org.bukkit.Material;

public interface GameScenario {
  String getName();

  String getKey();

  String[] getDescription();

  Material getMaterial();

  boolean isExperimental();
}
