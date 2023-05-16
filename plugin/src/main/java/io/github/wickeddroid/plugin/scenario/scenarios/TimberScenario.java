package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

@RegisteredScenario
@Scenario(name = "Timber", key = "timber", description = {}, material = Material.OAK_LOG)
public class TimberScenario extends ListenerScenario {

}

