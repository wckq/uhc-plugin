package io.github.wickeddroid.plugin.menu.settings;


import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
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
                .itemsPerRow(7)
                .skippedSlots(9, 17, 27, 35, 36, 44, 46, 52)
                .bounds(9, 45)
                .nextPageItem(page -> ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.ARROW)
                        .name(Component.text("Next page - " + page))
                        .build()))
                .previousPageItem(page -> ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.ARROW)
                        .name(Component.text("Previous page - " + page))
                        .build()))
                .itemIfNoNextPage(ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                                .name(Component.text(" ")).build()))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                                .name(Component.text(" ")).build()))
                .layoutLines(
                        "xxxxxxxxx",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "pxxxxxxxn"
                )
                .layoutItem('x', ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                                .build())
                );

        return menuInventory.build();
    }
}