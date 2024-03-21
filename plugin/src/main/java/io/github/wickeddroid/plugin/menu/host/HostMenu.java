package io.github.wickeddroid.plugin.menu.host;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.api.util.item.SimpleItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
import io.github.wickeddroid.plugin.menu.scenario.ScenariosInventory;
import io.github.wickeddroid.plugin.menu.settings.SettingsInventory;
import io.github.wickeddroid.plugin.menu.util.MenuUtils;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.scenario.SettingManager;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.adapt.MenuInventoryWrapper;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.List;

@Singleton
public class HostMenu extends UhcInventory {

    @Inject
    private Messages messages;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    private UhcGame uhcGame;
    @Inject
    private SettingManager settingManager;
    @Inject
    private ScenarioManager scenarioManager;
    @Inject
    private SettingsInventory settingsInventory;
    @Inject
    private ScenariosInventory scenariosInventory;

    @Inject
    public HostMenu(Messages messages, MessageHandler messageHandler) {
        super(PlainTextComponentSerializer.plainText().serialize(messageHandler.parse(messages.menu().getHostMenuTitle())), 6);
    }

    @Override
    public Inventory createInventory() {
        var inventory = MenuInventory.newPaginatedBuilder(ItemClickable.class, title);

        final ItemClickable hostItem = ItemClickable.builder().item(hostItemStack()).action(event -> {
            if(uhcGame.getHost() != null) {
                return true;
            } else {
                event.setCurrentItem(hostItemStack());
                uhcGame.setHost(event.getWhoClicked().getName());
                return true;
            }
        }).build();

        final ItemClickable settingItem = ItemClickable.builder().item(settingsItemStack()).action(event -> {
            event.getWhoClicked().openInventory(settingsInventory.createInventory());
            return true;
        }).build();

        final ItemClickable scenarioItem = ItemClickable.builder().item(scenariosItemStack()).action(event -> {
            event.getWhoClicked().openInventory(scenariosInventory.createInventory());
            return true;
        }).build();


        return inventory
                .entities(
                        List.of(
                                hostItem,
                                settingItem,
                                scenarioItem
                        )
                )
                .entityParser(itemClickable -> itemClickable)
                .skippedSlots(11, 19, 29, 37, 39, 21, 13, 23, 31, 41, 33, 43, 25, 15)
                .bounds(MenuUtils.PAGINATED_BOUND_FROM, MenuUtils.PAGINATED_BOUND_TO)
                .itemsPerRow(MenuUtils.PAGINATED_ROW_ITEMS_COUNT)
                .nextPageItem(MenuUtils::NEXT_PAGE)
                .previousPageItem(MenuUtils::PREVIOUS_PAGE)
                .itemIfNoNextPage(MenuUtils.NO_NEXT_PREVIOUS_PAGE)
                .itemIfNoPreviousPage(MenuUtils.NO_NEXT_PREVIOUS_PAGE)
                .itemIfNoEntities(ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.RED_STAINED_GLASS_PANE)
                        .name(MessageUtil.parseStringToComponent("<red>Componente Vacío"))
                        .build()))
                .layoutLines(
                        "xxxxxxxxx",
                        "xexexexex",
                        "xxexexexx",
                        "xexexexex",
                        "xxexexexx",
                        "xpxxxxxnx")
                .layoutItem('x', MenuUtils.BORDER_PANEL)
                .build();
    }

    private ItemStack settingsItemStack() {
        var itemBuilder = new SimpleItemBuilder(Material.CHAIN_COMMAND_BLOCK, 1);

        itemBuilder.name(messageHandler.parse(messages.menu().getSettingsButton()));
        itemBuilder.lore(List.of(messageHandler.parse(messages.menu().getSettingsButtonDescription(), String.valueOf(settingManager.getSettings().stream().filter(GameScenario::isEnabled).count()), String.valueOf(settingManager.getSettings().size()))));
        return itemBuilder.build();
    }

    private ItemStack scenariosItemStack() {
        var itemBuilder = new SimpleItemBuilder(Material.DIAMOND_SWORD, 1);

        itemBuilder.name(messageHandler.parse(messages.menu().getScenariosButton()));
        itemBuilder.lore(List.of(messageHandler.parse(messages.menu().getSettingsButtonDescription(), String.valueOf(scenarioManager.getScenarios().stream().filter(GameScenario::isEnabled).count()), String.valueOf(scenarioManager.getScenarios().size()))));
        return itemBuilder.build();
    }

    private ItemStack hostItemStack() {
        var itemBuilder = new SimpleItemBuilder(Material.PLAYER_HEAD, 1);

        itemBuilder.name(uhcGame.getHost() == null ? messageHandler.parse(messages.menu().getHostButtonUnselected()) : messageHandler.parse(messages.menu().getHostButtonSelected(), uhcGame.getHost()));
        if(uhcGame.getHost() != null) {
            itemBuilder.skull(Bukkit.getOfflinePlayer(uhcGame.getHost()));
        }

        return itemBuilder.build();
    }
}
