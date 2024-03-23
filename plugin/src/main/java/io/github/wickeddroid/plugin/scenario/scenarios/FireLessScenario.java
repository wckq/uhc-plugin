package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.LinkedList;
import java.util.List;

@RegisteredScenario
@Scenario(
        name = "FireLess",
        description = {
                "<gray>El fuego no hace daño."
        },
        key = "fire_less",
        material = Material.LAVA_BUCKET,
        supportsOptions = true
)
public class FireLessScenario extends ListenerScenario {

    @ScenarioOption(optionName = "prevent_damage_fire", dynamicValue = "preventFire")
    private LinkedList<OptionValue<Boolean>> damageFire = new LinkedList<>(
            List.of(
                    Option.buildValue(true, "Habilitado"),
                    Option.buildValue(false, "Deshabilitado")
            )
    );

    private Boolean preventFire;

    @ScenarioOption(optionName = "prevent_damage_lava", dynamicValue = "preventLava")
    private LinkedList<OptionValue<Boolean>> damageLava = new LinkedList<>(
            List.of(
                    Option.buildValue(true, "Habilitado"),
                    Option.buildValue(false, "Deshabilitado")
            )
    );

    private Boolean preventLava;

    @ScenarioOption(optionName = "prevent_damage_burn", dynamicValue = "preventBurn")
    private LinkedList<OptionValue<Boolean>> damageBurn = new LinkedList<>(
            List.of(
                    Option.buildValue(true, "Habilitado"),
                    Option.buildValue(false, "Deshabilitado")
            )
    );

    private Boolean preventBurn;

    @ScenarioOption(optionName = "prevent_damage_magma", dynamicValue = "preventMagma")
    private LinkedList<OptionValue<Boolean>> damageMagma = new LinkedList<>(
            List.of(
                    Option.buildValue(true, "Habilitado"),
                    Option.buildValue(false, "Deshabilitado")
            )
    );

    private Boolean preventMagma;

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }

    var cause = event.getCause();

    if(
            (preventFire && cause == EntityDamageEvent.DamageCause.FIRE) ||
            (preventLava && cause == EntityDamageEvent.DamageCause.LAVA) ||
            (preventBurn && cause == EntityDamageEvent.DamageCause.FIRE_TICK) ||
            (preventMagma && cause == EntityDamageEvent.DamageCause.HOT_FLOOR)
    ) {
      event.setDamage(0.0D);
      event.setCancelled(true);
    }

  }
}
