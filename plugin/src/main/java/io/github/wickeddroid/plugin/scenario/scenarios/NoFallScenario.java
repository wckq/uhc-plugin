package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@RegisteredScenario
@Scenario(
        name = "NoFall",
        key = "no_fall",
        description = {
                "<gray>- No hay daño de caída."
        },
        material = Material.DIAMOND_BOOTS
)
public class NoFallScenario extends ListenerScenario {

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
      return;
    }

    event.setDamage(0.0D);
    event.setCancelled(true);
  }
}
