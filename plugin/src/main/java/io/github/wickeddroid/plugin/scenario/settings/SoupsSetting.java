package io.github.wickeddroid.plugin.scenario.settings;

import io.github.wickeddroid.api.scenario.Setting;
import io.github.wickeddroid.plugin.scenario.RegisteredSetting;
import io.github.wickeddroid.plugin.scenario.SettingScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

@Setting(
        name = "Soups Off",
        description = "<gray>- Desactiva el consumo de las sopas sospechosas.",
        key = "soups",
        material = Material.SUSPICIOUS_STEW
)
@RegisteredSetting
public class SoupsSetting extends SettingScenario {

    @EventHandler
    public void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.SUSPICIOUS_STEW) {
            return;
        }

        event.setCancelled(true);
    }
}
