package io.github.wickeddroid.plugin.command.commandflow.factory;

import io.github.wickeddroid.plugin.command.commandflow.part.ScenarioPart;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ScenarioFactory implements PartFactory {

  private final ScenarioRegistration scenarioRegistration;

  public ScenarioFactory(final ScenarioRegistration scenarioRegistration) {
    this.scenarioRegistration = scenarioRegistration;
  }

  @Override
  public CommandPart createPart(
          final String name,
          final List<? extends Annotation> modifiers
  ) {
    return new ScenarioPart(name, scenarioRegistration);
  }
}
