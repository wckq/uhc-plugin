package io.github.wickeddroid.plugin.scenario.settings;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.scenario.Setting;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.scenario.RegisteredSetting;
import io.github.wickeddroid.plugin.scenario.SettingScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

import java.util.List;

@Setting(
        name = "Glistering Melon",
        description = {
                "<gray>- Cambia el crafteo del <gold>Glistering Melon Slice.",
                "<gold>   » <gray>Se craftea rodeado de lingotes de oro."

        },
        key = "glistering_melon",
        material = Material.GLISTERING_MELON_SLICE
)
@RegisteredSetting
public class GlisteringMelonSetting extends SettingScenario {

    @Inject private Plugin plugin;

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        var overrideVanilla = new ShapedRecipe(new NamespacedKey(plugin, "override_glistering_melon_slice"), ItemBuilder.newBuilder(Material.MELON_SLICE).build());
        overrideVanilla.shape("GGG", "GMG", "GGG");
        overrideVanilla.setIngredient('G', Material.GOLD_NUGGET);
        overrideVanilla.setIngredient('M', Material.MELON_SLICE);

        Bukkit.addRecipe(overrideVanilla);

        var nerfGlistering = new ShapedRecipe(new NamespacedKey(plugin, "glistering_melon_slice"), ItemBuilder.newBuilder(Material.GLISTERING_MELON_SLICE).build())
                .shape("GGG", "GMG", "GGG")
                .setIngredient('G', Material.GOLD_INGOT)
                .setIngredient('M', Material.MELON_SLICE);

        Bukkit.addRecipe(nerfGlistering);

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.discoverRecipes(List.of(overrideVanilla.getKey(), nerfGlistering.getKey()));
        });

        Bukkit.removeRecipe(NamespacedKey.minecraft("glistering_melon_slice"));
    }
}
