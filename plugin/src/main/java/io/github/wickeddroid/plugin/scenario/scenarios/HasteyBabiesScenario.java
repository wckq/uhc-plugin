package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.ScenarioOption;
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
        name = "HasteyBabies",
        key = "hastey_babies",
        description = {
                "<gray>- Las herramientas son encantadas con: ",
                "<aqua>   » Eficiencia I",
                "<aqua>   » Unbreaking I"
        },
        material = Material.IRON_PICKAXE,
        supportsOptions = true
)
public class HasteyBabiesScenario extends ListenerScenario {

  @ScenarioOption(optionName = "Fortuna I", dynamicValue = "fortune")
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

    var builder = ItemBuilder.newBuilder(item.getType())
            .enchantment(Enchantment.DIG_SPEED, 1)
            .enchantment(Enchantment.DURABILITY, 1);

    if(fortune) { builder.enchantment(Enchantment.LOOT_BONUS_BLOCKS, 1); }


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
                    .enchantment(Enchantment.DIG_SPEED, 1)
                    .enchantment(Enchantment.DURABILITY, 1)
                    .build()
    );
  }
}
