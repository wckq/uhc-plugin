package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

@RegisteredScenario
@Scenario(
        name = "Blood Diamonds",
        key = "blood_diamonds",
        description = {
                "<gray>- Los <aqua>Diamantes <gray>hacen daño",
                "<gray>al picarlos."
        },
        material = Material.DIAMOND,
        supportsOptions = true
)
public class BloodDiamondScenario extends ListenerScenario {

  @Override
  public void createOptions() {
    Option<Double> damageOptions = Option.createOption(
            "damage",
            1.0D,
            List.of(
                    new OptionValue<>(1.0, "0.5 ❤"),
                    new OptionValue<>(2.0, "1 ❤"),
                    new OptionValue<>(3.0, "1.5 ❤"),
                    new OptionValue<>(4.0, "2 ❤")
            )
    );

    this.addOptions(
            damageOptions
    );
  }


  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    if (event.getBlock().getType() == Material.DIAMOND_ORE
            || event.getBlock().getType() == Material.DEEPSLATE_DIAMOND_ORE) {
      event.getPlayer().damage(this.getOption("damage").value().getAsDouble());
    }
  }
}
