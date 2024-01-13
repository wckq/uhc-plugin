package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.events.GameStartEvent;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.plugin.event.game.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@RegisteredScenario
@Scenario(
        name = "Double HP",
        key = "double_hp",
        description = {
                "<gray>- Todos inician con 40 de HP."
        },
        material = Material.ENCHANTED_GOLDEN_APPLE
)
public class DoubleHPScenario extends ListenerScenario {
  @EventHandler
  public void onScatter(PlayerScatteredEvent event) {
    event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()+20.0D);
    event.getPlayer().setHealth(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
  }
}
