package io.github.wickeddroid.plugin.scenario;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import team.unnamed.inject.Inject;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import team.unnamed.inject.InjectAll;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@InjectAll
public class ScenarioManager {

  private Plugin plugin;
  private Messages messages;
  private MessageHandler messageHandler;
  private ScenarioRegistration scenarioRegistration;

  public void enableScenario(
          @Nullable final Player player,
          final String key
  ) {
    final var scenario = (ListenerScenario) this.scenarioRegistration.getScenarios().get(key);

    if (scenario == null && player != null) {
      this.messageHandler.send(player, this.messages.other().scenarioNotExists(), key);
      return;
    }

    scenario.setEnabled(true);
    Bukkit.getPluginManager().registerEvents(scenario, this.plugin);

    if(player != null) {
      this.messageHandler.send(player, this.messages.other().scenarioEnabled(), scenario.getName());
    }
  }

  public void disableScenario(
          final Player player,
          final String key
  ) {
    final var scenario = (ListenerScenario) this.scenarioRegistration.getScenarios().get(key);

    if (scenario == null) {
      this.messageHandler.send(player, this.messages.other().scenarioNotExists(), key);
      return;
    }

    scenario.setEnabled(false);
    this.messageHandler.send(player, this.messages.other().scenarioDisabled(), scenario.getName());
    HandlerList.unregisterAll(scenario);
  }

  public boolean isEnabled(String key) {
    return this.scenarioRegistration.getScenarios().get(key).isEnabled();
  }

  public @Nullable OptionValue<?> getOption(String key, String option) {
    if(!isEnabled(key)) { return null; }

    return this.scenarioRegistration.getScenarios().get(key).getOption(option).value();
  }

  public Collection<GameScenario> getScenarios() {
    return this.scenarioRegistration.getScenarios().values();
  }
}
