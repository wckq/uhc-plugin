package io.github.wickeddroid.plugin.menu.util;

import io.github.wickeddroid.api.util.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import team.unnamed.gui.menu.item.ItemClickable;

public class MenuUtils {

    public static final ItemClickable EMPTY_PANEL = ItemClickable.onlyItem(
            ItemBuilder.newBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                    .name(Component.text(" ")).build());

    public static final ItemClickable BORDER_PANEL = ItemClickable.onlyItem(
            ItemBuilder.newBuilder(Material.WHITE_STAINED_GLASS_PANE)
                    .name(Component.text(" ")).build());

    public static final ItemClickable NO_NEXT_PREVIOUS_PAGE = BORDER_PANEL;

    public static ItemClickable NEXT_PAGE(Integer page) {
        return ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.ARROW)
                .name(Component.text("Siguiente pagina - " + page)).build());
    }

    public static ItemClickable PREVIOUS_PAGE(Integer page) {
        return  ItemClickable.onlyItem(ItemBuilder.newBuilder(Material.ARROW)
                .name(Component.text("Anterior pagina - " + page))
                .build());
    }


    public static final int PAGINATED_BOUND_FROM = 10;
    public static final int PAGINATED_BOUND_TO = 43;
    public static final int PAGINATED_ROW_ITEMS_COUNT = 7;
}
