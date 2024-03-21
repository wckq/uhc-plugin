package io.github.wickeddroid.plugin.menu.scenario;

import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.menu.UhcInventory;
import io.github.wickeddroid.plugin.menu.host.HostMenu;
import io.github.wickeddroid.plugin.menu.util.MenuUtils;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScenariosInventory extends UhcInventory {

  @Inject private ScenarioManager scenarioManager;
  @Inject private HostMenu hostMenu;
  @Inject private ScenarioOptionsInventory scenarioOptionsInventory;

  private boolean redirect = false;
  public ScenariosInventory() {
    super("Scenarios", 6);
  }

  @Override
  public Inventory createInventory() {
    redirect = false;
    final var menuInventory = MenuInventory.newPaginatedBuilder(
            GameScenario.class, title
    );

    final var entities = scenarioManager.getScenarios().stream().sorted(Comparator.comparing(GameScenario::getName)).collect(Collectors.toList());

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

                      if(gameScenario.isEnabled() && gameScenario.isSupportsOptions()) {
                          if(event.isShiftClick()) {
                              redirect = true;
                              event.getWhoClicked().openInventory(scenarioOptionsInventory.createWithScenario(gameScenario));
                              return true;
                          }
                      }

                      if (!gameScenario.isEnabled()) {
                        scenarioManager.enableScenario(player, gameScenario.getKey());
                        currentItem.setItemMeta(enableItem(gameScenario).getItemMeta());
                        return true;
                      }

                      scenarioManager.disableScenario(player, gameScenario.getKey());
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
            .layoutItem('x', MenuUtils.BORDER_PANEL).closeAction(player -> {
                if(!redirect) {
                    player.openInventory(hostMenu.createInventory());
                }
            });

    return menuInventory.build();
  }

  private ItemStack enableItem(final GameScenario gameScenario) {
    return rawItem(gameScenario)
            .glowing(true)
            .build();
  }

  private ItemBuilder rawItem(final GameScenario gameScenario) {
    List<String> loreList = new ArrayList<>(Arrays.asList(gameScenario.getDescription()));

    if(gameScenario.isSupportsOptions()) {
        loreList.add("");
        loreList.add("<green>Shift <gray>+ <green>Click Izq. <gray>para modificar opciones.");
    }

    return ItemBuilder.newBuilder(gameScenario.getMaterial())
            .name(MessageUtil.parseStringToComponent((gameScenario.isEnabled() ? "<color:#08992E>✅ " : "<color:#E01616>❌ ") + "<color:#93FF9E>" + gameScenario.getName())
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            .lore(loreList.stream()
                    .map(lore -> MessageUtil.parseStringToComponent(lore)
                            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                    .collect(Collectors.toList()));
  }
}
