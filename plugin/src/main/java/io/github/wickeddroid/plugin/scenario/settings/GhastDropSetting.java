package io.github.wickeddroid.plugin.scenario.settings;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.scenario.Setting;
import io.github.wickeddroid.plugin.scenario.RegisteredSetting;
import io.github.wickeddroid.plugin.scenario.SettingScenario;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

@Setting(
        name = "Ghast Drop",
        description = {
                "<gray>- Los drops de Ghast se reemplazan por lingotes de oro."
        },
        key = "ghast_drop",
        material = Material.GHAST_TEAR
)
@RegisteredSetting
public class GhastDropSetting extends SettingScenario {

    @Inject
    private UhcGame uhcGame;

    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if(event.getEntityType() != EntityType.GHAST) { return; }
        if(event.getDrops().isEmpty()) { return; }

        event.getDrops().clear();
        event.getDrops().add(new ItemStack(Material.GOLD_INGOT));
    }
}