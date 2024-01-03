package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

@RegisteredScenario
@Scenario(
        name = "FireLess",
        description = {""},
        key = "fire_less",
        material = Material.LAVA_BUCKET,
        supportsOptions = true
)
public class FireLessScenario extends ListenerScenario {


  @Override
  public void createOptions() {
    Option<Boolean> fireCauseOption = Option.createOption(
            "prevent_damage_fire",
            true,
            List.of(
                    new OptionValue<>(true, "Activo"),
                    new OptionValue<>(false, "Deshabilitado")
            )
    );

    Option<Boolean> lavaCauseOption = Option.createOption(
            "prevent_damage_lava",
            true,
            List.of(
                    new OptionValue<>(true, "Activo"),
                    new OptionValue<>(false, "Deshabilitado")
            )
    );

    Option<Boolean> burnCauseOption = Option.createOption(
            "prevent_damage_burn",
            true,
            List.of(
                    new OptionValue<>(true, "Activo"),
                    new OptionValue<>(false, "Deshabilitado")
            )
    );

    Option<Boolean> hotFloorOption = Option.createOption(
            "prevent_damage_magma",
            true,
            List.of(
                    new OptionValue<>(true, "Activo"),
                    new OptionValue<>(false, "Deshabilitado")
            )
    );

    this.addOptions(
            fireCauseOption,
            lavaCauseOption,
            burnCauseOption,
            hotFloorOption
    );
  }

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }

    var cause = event.getCause();

    if(
            (getOption("prevent_damage_fire").value().getAsBoolean() && cause == EntityDamageEvent.DamageCause.FIRE) ||
            (getOption("prevent_damage_lava").value().getAsBoolean() && cause == EntityDamageEvent.DamageCause.LAVA) ||
            (getOption("prevent_damage_burn").value().getAsBoolean() && cause == EntityDamageEvent.DamageCause.FIRE_TICK) ||
            (getOption("prevent_damage_magma").value().getAsBoolean() && cause == EntityDamageEvent.DamageCause.HOT_FLOOR)
    ) {
      event.setDamage(0.0D);
      event.setCancelled(true);
    }

  }
}
