package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.event.player.PlayerScatteredEvent;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.api.scenario.options.ScenarioOption;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RegisteredScenario
@Scenario(
        name = "Starter Item",
        key = "starter_item",
        description = {
                "<gray>- Los jugadores pueden iniciar con un item al azar:",
                "<gold>   » Bundle",
                "<gold>   » Golden Apple",
                "<gold>   » Shulker Box",
                "<gold>   » Random (Anteriores 3)",
                "<gold>   » Random (Cualquier Item)"
        },
        material = Material.NETHER_STAR,
        supportsOptions = true
)
public class StarterItemScenario extends ListenerScenario {

    @ScenarioOption(optionName = "Starter Item", dynamicValue = "material")
    private LinkedList<OptionValue<Material>> optionValues = Option.createValues(
            Material.AIR, "Selección Random",
            Material.STRUCTURE_VOID, "Random (Todos los items)",
            Material.BUNDLE, "Mochila",
            Material.GOLDEN_APPLE, "Manzana Dorada",
            Material.SHULKER_BOX, "Shulker",
            Material.ENCHANTED_GOLDEN_APPLE, "Notch"
    );

    private Material material = null;

    @EventHandler
    public void onStart(GameStartEvent event) {
        var mats = Arrays.stream(Material.values()).filter(Material::isItem).filter(m -> !m.isLegacy()).toList();
        var item = material == Material.AIR ? new ItemStack(List.of(Material.BUNDLE, Material.GOLDEN_APPLE, Material.SHULKER_BOX, Material.ENCHANTED_GOLDEN_APPLE).get(ThreadLocalRandom.current().nextInt(4))) : material == Material.STRUCTURE_VOID ? new ItemStack(mats.get(ThreadLocalRandom.current().nextInt(mats.size()))): new ItemStack(material);

        Bukkit.getOnlinePlayers().forEach(
                p -> p.getInventory().addItem(item)
        );
    }

    @EventHandler
    public void onScatter(PlayerScatteredEvent event) {
        if(!event.isLaterScatter()) { return; }

        var mats = Arrays.stream(Material.values()).filter(Material::isItem).filter(m -> !m.isLegacy()).toList();
        var item = material == Material.AIR ? new ItemStack(List.of(Material.BUNDLE, Material.GOLDEN_APPLE, Material.SHULKER_BOX, Material.ENCHANTED_GOLDEN_APPLE).get(ThreadLocalRandom.current().nextInt(4))) : material == Material.STRUCTURE_VOID ? new ItemStack(mats.get(ThreadLocalRandom.current().nextInt(mats.size()))): new ItemStack(material);

        event.getPlayer().getInventory().addItem(item);
    }
}
