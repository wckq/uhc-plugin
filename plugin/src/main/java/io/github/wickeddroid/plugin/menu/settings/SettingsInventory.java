package io.github.wickeddroid.plugin.menu.settings;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.internal.WorkInProgress;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
import io.github.wickeddroid.plugin.menu.host.HostMenu;
import io.github.wickeddroid.plugin.menu.util.MenuUtils;
import io.github.wickeddroid.plugin.scenario.SettingManager;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.inject.Inject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class SettingsInventory extends UhcInventory {

    @Inject private SettingManager settingManager;
    @Inject private HostMenu hostMenu;

    public SettingsInventory() {
        super("Settings", 6);
    }

    @Override
    public Inventory createInventory() {
        final var menuInventory = MenuInventory.newPaginatedBuilder(
                GameScenario.class, title, 6
        );

        final var entities = settingManager.getSettings().stream().filter(scenario -> !scenario.getClass().isAnnotationPresent(WorkInProgress.class)).sorted(Comparator.comparing(GameScenario::getName)).collect(Collectors.toList());

        menuInventory.entities(entities)
                .entityParser(gameScenario -> ItemClickable.builder()
                        .item(gameScenario.isEnabled() ? enableItem(gameScenario) : rawItem(gameScenario).build())
                        .action(event -> {
                            final var currentItem = event.getCurrentItem();

                            if (currentItem == null) {
                                return true;
                            }

                            if (!(event.getWhoClicked() instanceof Player player)) {
                                return true;
                            }

                            if (!gameScenario.isEnabled()) {
                                settingManager.enableSetting(player, gameScenario.getKey());
                                currentItem.setItemMeta(enableItem(gameScenario).getItemMeta());
                                return true;
                            }

                            settingManager.disableSetting(player, gameScenario.getKey());
                            currentItem.setItemMeta(rawItem(gameScenario).build().getItemMeta());
                            return true;
                        })
                        .build()
                )
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
                .layoutItem('x', MenuUtils.BORDER_PANEL)
                .closeAction(player -> {
                    player.openInventory(hostMenu.createInventory());
                }).build();

        return menuInventory.build();
    }

    private ItemStack enableItem(final GameScenario gameScenario) {
        return rawItem(gameScenario)
                .glowing(true)
                .build();
    }

    private ItemBuilder rawItem(final GameScenario gameScenario) {
        return ItemBuilder.newBuilder(gameScenario.getMaterial())
                .name(MessageUtil.parseStringToComponent((gameScenario.isEnabled() ? "<color:#08992E>✅ " : "<color:#E01616>❌ ")+ "<color:#93FF9E>" + gameScenario.getName())
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .lore(Arrays.stream(gameScenario.getDescription())
                        .map(lore -> MessageUtil.parseStringToComponent(lore)
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                        .collect(Collectors.toList()));
    }
}