package io.github.wickeddroid.plugin.scenario.settings;

import io.github.wickeddroid.api.events.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.scenario.Setting;
import io.github.wickeddroid.plugin.scenario.RegisteredSetting;
import io.github.wickeddroid.plugin.scenario.SettingScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import team.unnamed.inject.Inject;

@Setting(
        name = "Team Inventory",
        description = {
                "<gray>- Activa el Team Inventory.",
                "<gold>   » <green>/ti"
        },
        key = "team_inventory",
        material = Material.ENDER_CHEST
)
@RegisteredSetting
public class TeamInventorySetting extends SettingScenario {

    @Inject
    private UhcGame uhcGame;

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        this.uhcGame.setTeamInventory(true);
    }
}