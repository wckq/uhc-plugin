package io.github.wickeddroid.plugin.scenario;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import team.unnamed.inject.Inject;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;

public class ScenarioManager {

  @Inject private Plugin plugin;
  @Inject private ScenarioRegistration scenarioRegistration;

  public void enableScenario(final String key) {
    final var scenario = (ListenerScenario) this.scenarioRegistration.getScenarios().get(key);

    Bukkit.getPluginManager().registerEvents(scenario, this.plugin);
  }

  public void disableScenario(final String key) {
    final var scenario = (ListenerScenario) this.scenarioRegistration.getScenarios().get(key);

    HandlerList.unregisterAll(scenario);
  }
}
