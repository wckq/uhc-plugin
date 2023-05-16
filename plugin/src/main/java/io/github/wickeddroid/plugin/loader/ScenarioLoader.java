package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import team.unnamed.inject.Inject;

public class ScenarioLoader implements Loader {
  @Inject private ScenarioRegistration scenarioRegistration;

  @Override
  public void load() {
    this.scenarioRegistration.registerScenarios();
  }
}
