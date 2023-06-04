package io.github.wickeddroid.plugin.command.commandflow.part;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScenarioPart implements ArgumentPart {

  private final String name;
  private final ScenarioRegistration scenarioRegistration;

  public ScenarioPart(
          final String name,
          final ScenarioRegistration scenarioRegistration
  ) {
    this.name = name;
    this.scenarioRegistration = scenarioRegistration;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public List<GameScenario> parseValue(
          final CommandContext context,
          final ArgumentStack stack,
          final @Nullable CommandPart caller
  ) throws ArgumentParseException {
    final var possibleScenario = stack.next().toLowerCase();

    final var scenario = this.scenarioRegistration.getScenarios().get(possibleScenario);

    if (scenario == null) {
      throw new ArgumentParseException(":v");
    }

    return Collections.singletonList(scenario);
  }

  @Override
  public @Nullable List<String> getSuggestions(
          final CommandContext commandContext,
          final ArgumentStack stack
  ) {
    final var next = stack.hasNext() ? stack.next() : null;

    if (next == null) {
      return Collections.emptyList();
    }

    final var suggestions = new ArrayList<String>();

    for (final var scenario : this.scenarioRegistration.getScenarios().values()) {
      if (scenario.getKey().isEmpty() || scenario.getKey().startsWith(next)) {
        suggestions.add(scenario.getKey());
      }
    }

    return suggestions;
  }
}
