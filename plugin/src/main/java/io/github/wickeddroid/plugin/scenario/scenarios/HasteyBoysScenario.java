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
        name = "HasteyBoys",
        key = "hastey_boys",
        description = {
                "<gray>- Las herramientas son encantadas con: ",
                "<aqua>   » Eficiencia III",
                "<aqua>   » Unbreaking I"
        },
        material = Material.DIAMOND_PICKAXE,
        supportsOptions = true
)
public class  HasteyBoysScenario extends ListenerScenario {

  @ScenarioOption(optionName = "Fortuna II", dynamicValue = "fortune")
  private LinkedList<OptionValue<Boolean>> fortuneOption = new LinkedList<>(
          List.of(
                  Option.buildValue(false, "Deshabilitado"),
                  Option.buildValue(true, "Habilitado")
          )
  );

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

    var builder =  ItemBuilder.newBuilder(item.getType())
            .enchantment(Enchantment.DIG_SPEED, 3)
            .enchantment(Enchantment.DURABILITY, 1);

    if(fortune) { builder.enchantment(Enchantment.LOOT_BONUS_BLOCKS, 2); }

    event.getInventory().setResult(builder.build());
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


    event.setCurrentItem(
            ItemBuilder.newBuilder(item.getType())
                    .enchantment(Enchantment.DIG_SPEED, 3)
                    .enchantment(Enchantment.DURABILITY, 1)
                    .build()
    );
  }
}
