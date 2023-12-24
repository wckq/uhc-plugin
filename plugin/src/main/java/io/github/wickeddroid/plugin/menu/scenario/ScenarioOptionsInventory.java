package io.github.wickeddroid.plugin.menu.scenario;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.util.EnhancedIterator;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioOptionsInventory extends UhcInventory {
    private final GameScenario scenario;
    private final ScenariosInventory oldInv;

    public ScenarioOptionsInventory(GameScenario scenario, ScenariosInventory oldInv) {
        super("Opciones de " + scenario.getName(), 6);

        this.scenario = scenario;
        this.oldInv = oldInv;
    }

    @Override
    public Inventory createInventory() {
        final var menuInventory = MenuInventory.newPaginatedBuilder(
                Option.class, title
        );


        final List<Option> entities = new ArrayList<>(scenario.getOptions().values());

        menuInventory.entities(entities)
                .entityParser(option -> ItemClickable.builder()
                        .item(item(option))
                        .action(event -> {
                            var iterator = new EnhancedIterator<OptionValue<?>>(option.options());

                            iterator.setPosition(option.value());

                            if(!iterator.hasNext()) {
                                iterator.restart();
                            } else {
                                iterator.next();
                            }

                            option.setValue(iterator.current());
                            event.getCurrentItem().setItemMeta(item(option).getItemMeta());

                            return true;
                        }).build())
                .bounds(9, 45)
                .itemIfNoNextPage(ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                        .name(Component.text(" "))
                        .build()))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                        .name(Component.text(" "))
                        .build()))
                .nextPageItem(page -> ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.ARROW)
                        .name(Component.text("Siguiente pagina - " + page))
                        .build()))
                .previousPageItem(page -> ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.ARROW)
                        .name(Component.text("Anterior pagina - " + page))
                        .build()))
                .itemIfNoNextPage(ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                                .name(Component.text(" ")).build()))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                                .name(Component.text(" ")).build()))
                .layoutLines(
                        "xxxxxxxxx",
                        "eeeeeeeee",
                        "eeeeeeeee",
                        "eeeeeeeee",
                        "eeeeeeeee",
                        "pxxxcxxxn"
                )
                .layoutItem('x', ItemClickable.onlyItem(
                        ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                                .name(Component.text(" "))
                                .build())
                )
                .layoutItem('c', ItemClickable.builder()
                        .item(ItemBuilder.newBuilder(Material.BARRIER).name(MessageUtil.parseStringToComponent("<dark_red>Volver")).build())
                        .action(event -> {
                            event.getInventory().close();
                            event.getWhoClicked().openInventory(oldInv.createInventory());
                            return true;
                        }).build()
                );

        return menuInventory.build();
    }


    private ItemStack item(Option<?> option) {
        return ItemBuilder.newBuilder(Material.CHAIN_COMMAND_BLOCK)
                .name(MessageUtil.parseStringToComponent("<color:#93FF9E>" + option.optionName() + " option")
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .lore(Arrays.stream(new String[]{"<red>Opción Actual: <gold>" + option.value().getValueDisplay(), "", "<gray>¡Click para alternar!"})
                        .map(lore -> MessageUtil.parseStringToComponent(lore)
                                .color(TextColor.color(255, 255, 255))
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                        .collect(Collectors.toList())).build();
    }
}
