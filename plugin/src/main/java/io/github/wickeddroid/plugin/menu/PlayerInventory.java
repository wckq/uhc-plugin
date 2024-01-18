package io.github.wickeddroid.plugin.menu;

import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.util.MessageUtil;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PlayerInventory {

    private Inventory inv;

    public void openInv(Player target, Player player) {
        this.inv = Bukkit.createInventory(null,54, MessageUtil.parseStringToComponent("<red>Inventario de " + target.getName()));
        configInv(target);

        player.openInventory(inv);
    }

    private void configInv(Player target) {
        ItemStack empty = ItemBuilder.newBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(MessageUtil.parseStringToComponent("<gold>Vacío")).build();


        inv.setItem(9, target.getInventory().getHelmet() == null ? ItemBuilder.newBuilder(Material.RED_STAINED_GLASS_PANE).name(MessageUtil.parseStringToComponent("<gold>Casco")).build() : target.getInventory().getHelmet());
        inv.setItem(10, target.getInventory().getChestplate() == null ? ItemBuilder.newBuilder(Material.RED_STAINED_GLASS_PANE).name(MessageUtil.parseStringToComponent("<gold>Pechera")).build() : target.getInventory().getChestplate());
        inv.setItem(11, target.getInventory().getLeggings() == null ? ItemBuilder.newBuilder(Material.RED_STAINED_GLASS_PANE).name(MessageUtil.parseStringToComponent("<gold>Pantalones")).build() : target.getInventory().getLeggings());
        inv.setItem(12, target.getInventory().getBoots() == null ? ItemBuilder.newBuilder(Material.RED_STAINED_GLASS_PANE).name(MessageUtil.parseStringToComponent("<gold>Botas")).build() : target.getInventory().getBoots());
        inv.setItem(13, target.getInventory().getItemInOffHand().getType() == Material.AIR ?ItemBuilder.newBuilder(Material.RED_STAINED_GLASS_PANE).name(MessageUtil.parseStringToComponent("<gold>Segunda Mano")).build() : target.getInventory().getItemInOffHand());

        inv.setItem(18, target.getInventory().getItem(0));
        inv.setItem(19, target.getInventory().getItem(1));
        inv.setItem(20, target.getInventory().getItem(2));
        inv.setItem(21, target.getInventory().getItem(3));
        inv.setItem(22, target.getInventory().getItem(4));
        inv.setItem(23, target.getInventory().getItem(5));
        inv.setItem(24, target.getInventory().getItem(6));
        inv.setItem(25, target.getInventory().getItem(7));
        inv.setItem(26, target.getInventory().getItem(8));
        inv.setItem(27, target.getInventory().getItem(27));
        inv.setItem(28, target.getInventory().getItem(28));
        inv.setItem(29, target.getInventory().getItem(29));
        inv.setItem(30, target.getInventory().getItem(30));
        inv.setItem(31, target.getInventory().getItem(31));
        inv.setItem(32, target.getInventory().getItem(32));
        inv.setItem(33, target.getInventory().getItem(33));
        inv.setItem(34, target.getInventory().getItem(34));
        inv.setItem(35, target.getInventory().getItem(35));
        inv.setItem(36, target.getInventory().getItem(18));
        inv.setItem(37, target.getInventory().getItem(19));
        inv.setItem(38, target.getInventory().getItem(20));
        inv.setItem(39, target.getInventory().getItem(21));
        inv.setItem(40, target.getInventory().getItem(22));
        inv.setItem(41, target.getInventory().getItem(23));
        inv.setItem(42, target.getInventory().getItem(24));
        inv.setItem(43, target.getInventory().getItem(25));
        inv.setItem(44, target.getInventory().getItem(26));
        inv.setItem(45, target.getInventory().getItem(9));
        inv.setItem(46, target.getInventory().getItem(10));
        inv.setItem(47, target.getInventory().getItem(11));
        inv.setItem(48, target.getInventory().getItem(12));
        inv.setItem(49, target.getInventory().getItem(13));
        inv.setItem(50, target.getInventory().getItem(14));
        inv.setItem(51, target.getInventory().getItem(15));
        inv.setItem(52, target.getInventory().getItem(16));
        inv.setItem(53, target.getInventory().getItem(17));

        for(int i = 0; i < 54; i++) {
            if(inv.getItem(i) == null) {
                inv.setItem(i, empty);
            }
        }
    }
}
