package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.util.MaterialUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import java.util.LinkedList;
import java.util.List;

@RegisteredScenario
@Scenario(
        name = "HasteyBoys+",
        key = "hastey_boys_plus",
        description = {
                "<gray>- Las herramientas son encantadas con: ",
                "<aqua>   » Eficiencia V",
                "<aqua>   » Unbreaking III"
        },
        material = Material.NETHERITE_PICKAXE,
        supportsOptions = true
)
public class HasteyBoysPlusScenario extends ListenerScenario {

  @ScenarioOption(optionName = "Fortuna III", dynamicValue = "fortune")
  private LinkedList<OptionValue<Boolean>> fortuneOption = Option.buildBooleanValues(false);

  private Boolean fortune;

  @EventHandler
  public void onPrepareItemCraft(final PrepareItemCraftEvent event) {
    final var recipe = event.getRecipe();

    if (recipe == null) {
      return;
    }

    final var item = recipe.getResult();

    if (!MaterialUtil.isTool(item.getType())) {
      return;
    }

    event.getInventory().setResult(
            ItemBuilder.newBuilder(item.getType())
                    .enchantment(Enchantment.DIG_SPEED, 5)
                    .enchantment(Enchantment.DURABILITY, 3)
                    .build()
    );
  }

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent event) {
    final var item = event.getCurrentItem();

    if (item == null
            || item.hasItemMeta()
            || !MaterialUtil.isTool(item.getType())
            || (item.containsEnchantment(Enchantment.DIG_SPEED) || item.containsEnchantment(Enchantment.DURABILITY))) {
      return;
    }

    var builder =   ItemBuilder.newBuilder(item.getType())
            .enchantment(Enchantment.DIG_SPEED, 5)
            .enchantment(Enchantment.DURABILITY, 3);

    if(fortune) { builder.enchantment(Enchantment.LOOT_BONUS_BLOCKS, 3); }

    event.setCurrentItem(builder.build());
  }
}
