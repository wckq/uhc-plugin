package io.github.wickeddroid.plugin.scenario.settings;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.scenario.Setting;
import io.github.wickeddroid.plugin.scenario.RegisteredSetting;
import io.github.wickeddroid.plugin.scenario.SettingScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import team.unnamed.inject.Inject;

@Setting(
        name = "CleanItem",
        description = {
                "<gray>- Activa poder borrarle encantamientos a items.",
                "<gold>   » <green>/cleanitem"
        },
        key = "clean_item",
        material = Material.GRINDSTONE
)
@RegisteredSetting
public class CleanItemSetting extends SettingScenario {

    @Inject
    private UhcGame uhcGame;

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        this.uhcGame.setCleanItem(true);
    }
}