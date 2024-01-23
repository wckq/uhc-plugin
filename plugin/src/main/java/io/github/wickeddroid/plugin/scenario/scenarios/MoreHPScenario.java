package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.event.player.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;

import java.util.LinkedList;
import java.util.List;

@RegisteredScenario
@Scenario(
        name = "More HP",
        key = "double_hp",
        description = {
                "<gray>- Todos inician con más HP."
        },
        material = Material.EMERALD_BLOCK,
        supportsOptions = true
)
public class MoreHPScenario extends ListenerScenario {

  @ScenarioOption(optionName = "Extra HP", dynamicValue = "hp")
  private LinkedList<OptionValue<Double>> extraHPOption = new LinkedList<>(
          List.of(
                  Option.buildValue(10.0D, "+5 ❤"),
                  Option.buildValue(20.0D, "+10 ❤"),
                  Option.buildValue(30.0D, "+15 ❤"),
                  Option.buildValue(40.0D, "+20 ❤"),
                  Option.buildValue(50.0D, "+25 ❤"),
                  Option.buildValue(60.0D, "+30 ❤")
          )
  );

  private Double hp;

  @EventHandler
  public void onScatter(PlayerScatteredEvent event) {
    event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()+hp);
    event.getPlayer().setHealth(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
  }
}
