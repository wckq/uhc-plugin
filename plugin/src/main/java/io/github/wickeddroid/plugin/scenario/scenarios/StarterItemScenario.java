package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.events.GameStartEvent;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
        material = Material.BUNDLE,
        supportsOptions = true
)
public class StarterItemScenario extends ListenerScenario {

    @Override
    public void createOptions() {
        Option<Material> itemOptions = Option.createOption(
                "Starter Item",
                Material.AIR,
                Option.createValues(
                        Material.AIR, "Selección Random",
                        Material.STRUCTURE_VOID, "Random (Todos los items)",
                        Material.BUNDLE, "Mochila",
                        Material.GOLDEN_APPLE, "Manzana Dorada",
                        Material.SHULKER_BOX, "Shulker"
                )
        );

        this.addOptions(itemOptions);
    }

    @EventHandler
    public void onStart(GameStartEvent event) {
        var material = this.getOption("Starter Item").value().getAsMaterial();

        var mats = Arrays.stream(Material.values()).filter(Material::isItem).filter(m -> !m.isLegacy()).toList();
        var item = material == Material.AIR ? new ItemStack(List.of(Material.BUNDLE, Material.GOLDEN_APPLE, Material.SHULKER_BOX).get(ThreadLocalRandom.current().nextInt(3))) : material == Material.STRUCTURE_VOID ? new ItemStack(mats.get(ThreadLocalRandom.current().nextInt(mats.size()))): new ItemStack(material);

        Bukkit.getOnlinePlayers().forEach(
                p -> p.getInventory().addItem(item)
        );
    }
}
