package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.LinkedList;
import java.util.List;

@RegisteredScenario
@Scenario(
        name = "Blood Diamonds",
        key = "blood_diamonds",
        description = {
                "<gray>- Los <aqua>Diamantes <gray>hacen daño",
                "<gray>al picarlos."
        },
        material = Material.DIAMOND_ORE,
        supportsOptions = true
)
public class BloodDiamondScenario extends ListenerScenario {

  @ScenarioOption(optionName = "Daño", dynamicValue = "damage")
  private LinkedList<OptionValue<Double>> dañoValue = new LinkedList<>(
          List.of(
                  Option.buildValue(1.0, "0.5 ❤"),
                  Option.buildValue(2.0, "1 ❤"),
                  Option.buildValue(3.0, "1.5 ❤"),
                  Option.buildValue(4.0, "2 ❤")
          )
  );

  private Double damage;

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    if (event.getBlock().getType() == Material.DIAMOND_ORE
            || event.getBlock().getType() == Material.DEEPSLATE_DIAMOND_ORE) {
      event.getPlayer().damage(damage);
    }
  }
}
