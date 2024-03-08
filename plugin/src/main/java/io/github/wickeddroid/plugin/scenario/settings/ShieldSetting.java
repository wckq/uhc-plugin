package io.github.wickeddroid.plugin.scenario.settings;

import io.github.wickeddroid.api.scenario.Setting;
import io.github.wickeddroid.plugin.scenario.RegisteredSetting;
import io.github.wickeddroid.plugin.scenario.SettingScenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@Setting(
        name = "Shieldless",
        description = "<gray>- Desactiva los escudos.",
        key = "shieldless",
        material = Material.SHIELD
)
@RegisteredSetting
public class ShieldSetting extends SettingScenario {


    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if(event.getRecipe().getResult().getType() == Material.SHIELD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if(event.hasItem()) {
            if(event.getItem().getType() == Material.SHIELD) {
                event.setCancelled(true);
                event.getPlayer().setShieldBlockingDelay(20*300);
                event.getPlayer().setCooldown(Material.SHIELD, 20*300);
            }
        }
    }
}
