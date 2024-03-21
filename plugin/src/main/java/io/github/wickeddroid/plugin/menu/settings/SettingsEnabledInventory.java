package io.github.wickeddroid.plugin.menu.settings;


import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
import io.github.wickeddroid.plugin.menu.util.MenuUtils;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.scenario.SettingManager;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.inject.Inject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SettingsEnabledInventory extends UhcInventory {

    @Inject private SettingManager settingManager;

    public SettingsEnabledInventory() {
        super("Settings activas", 6);
    }

    @Override
    public Inventory createInventory() {
        final var menuInventory = MenuInventory.newPaginatedBuilder(
                GameScenario.class, title
        );

        final var entities = settingManager.getSettings()
                .stream()
                .filter(GameScenario::isEnabled)
                .collect(Collectors.toList());

        menuInventory.entities(entities)
                .entityParser(gameScenario -> ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(gameScenario.getMaterial())
                                .name(MessageUtil.parseStringToComponent("<color:#93FF9E>" + gameScenario.getName())
                                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                                .lore(Arrays.stream(gameScenario.getDescription())
                                        .map(lore -> MessageUtil.parseStringToComponent(lore)
                                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                                        .collect(Collectors.toList()))
                                .build()
                ))
                .bounds(MenuUtils.PAGINATED_BOUND_FROM, MenuUtils.PAGINATED_BOUND_TO)
                .itemsPerRow(MenuUtils.PAGINATED_ROW_ITEMS_COUNT)
                .nextPageItem(MenuUtils::NEXT_PAGE)
                .previousPageItem(MenuUtils::PREVIOUS_PAGE)
                .itemIfNoNextPage(MenuUtils.NO_NEXT_PREVIOUS_PAGE)
                .itemIfNoPreviousPage(MenuUtils.NO_NEXT_PREVIOUS_PAGE)
                .fillBorders(MenuUtils.BORDER_PANEL)
                .layoutLines(
                        "xxxxxxxxx",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "pxxxxxxxn"
                )
                .layoutItem('x', MenuUtils.BORDER_PANEL);

        return menuInventory.build();
    }
}