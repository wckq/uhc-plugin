package io.github.wickeddroid.plugin.command.commandflow;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.plugin.command.commandflow.factory.ScenarioFactory;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import me.fixeddev.commandflow.annotated.part.AbstractModule;

public class UhcCommandModule extends AbstractModule {

  private final ScenarioRegistration scenarioRegistration;

  public UhcCommandModule(final ScenarioRegistration scenarioRegistration) {
    this.scenarioRegistration = scenarioRegistration;
  }

  @Override
  public void configure() {
    this.bindFactory(GameScenario.class, new ScenarioFactory(this.scenarioRegistration));
  }
}
