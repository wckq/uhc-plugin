package io.github.wickeddroid.plugin.menu.scenario;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.util.EnhancedIterator;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
import io.github.wickeddroid.plugin.menu.util.MenuUtils;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioOptionsInventory extends UhcInventory {

    @Inject
    private ScenariosInventory scenariosInventory;

    public ScenarioOptionsInventory() {
        super("Opciones de Scenario", 6);
    }

    private GameScenario scenario;

    public Inventory createWithScenario(GameScenario scenario) {
        this.scenario = scenario;

        return createInventory();
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

                            var next = event.getClick() != ClickType.RIGHT;

                            iterator.setPosition(option.value());

                            if(next) {
                                if(!iterator.hasNext()) {
                                    iterator.restart();
                                } else {
                                    iterator.next();
                                }
                            } else {
                                if(!iterator.hasPrevious()) {
                                    iterator.finish();
                                } else {
                                    iterator.previous();
                                }
                            }

                            var value = iterator.current();

                            option.setValue(scenario, value);
                            event.getCurrentItem().setItemMeta(item(option).getItemMeta());

                            return true;
                        }).build())
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
                    player.openInventory(scenariosInventory.createInventory());
                });

        return menuInventory.build();
    }


    private ItemStack item(Option<?> option) {
        return ItemBuilder.newBuilder(Material.CHAIN_COMMAND_BLOCK)
                .name(MessageUtil.parseStringToComponent("<color:#93FF9E>" + option.optionName())
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .lore(Arrays.stream(new String[]
                                {"<red>Opción Actual: <gold>" + option.value().getValueDisplay(), "", "<gray>» Click Izquierdo para <bold>Avanzar", "<gray>» Click Derecho para <bold>Retroceder"}
                        )
                        .map(lore -> MessageUtil.parseStringToComponent(lore)
                                .color(TextColor.color(255, 255, 255))
                                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                        .collect(Collectors.toList())).build();
    }
}
