package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;

@RegisteredScenario
@Scenario(
        name = "Uhc España",
        key = "uhc_spain",
        description = {
                "<gray>- Puedes craftear:",
                "<gold>   » <bold>Super Golden Apples",
                "<gold>   » <bold>Hyper Goldens Apples"
        },
        material = Material.GOLDEN_APPLE
)
public class UhcSpainScenario extends ListenerScenario {

    @Inject
    private Plugin plugin;

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        final var superGoldenApple = ItemBuilder
                .newBuilder(Material.GOLDEN_APPLE)
                .name(MessageUtil.parseStringToComponent("<yellow>Super Golden Apple").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .modelData(19)
                .build();

        final var hyperGoldenApple = ItemBuilder
                .newBuilder(Material.GOLDEN_APPLE)
                .name(MessageUtil.parseStringToComponent("<yellow>Hyper Golden Apple").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .modelData(20)
                .build();

        final var superGoldenAppleRecipe = new ShapedRecipe(new NamespacedKey(plugin, "super_golden_apple"), superGoldenApple);

        superGoldenAppleRecipe.shape("XXX", "XGX", "XXX");
        superGoldenAppleRecipe.setIngredient('X', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT));
        superGoldenAppleRecipe.setIngredient('G', new RecipeChoice.MaterialChoice(Material.GOLDEN_APPLE));

        Bukkit.addRecipe(superGoldenAppleRecipe);

        final var hyperGoldenAppleRecipe = new ShapedRecipe(new NamespacedKey(plugin, "hyper_golden_apple"), hyperGoldenApple);

        hyperGoldenAppleRecipe.shape("XXX", "XGX", "XXX");
        hyperGoldenAppleRecipe.setIngredient('X', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT));
        hyperGoldenAppleRecipe.setIngredient('G', new RecipeChoice.ExactChoice(superGoldenApple));

        Bukkit.addRecipe(hyperGoldenAppleRecipe);

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.discoverRecipe(new NamespacedKey(plugin, "super_golden_apple"));
            p.discoverRecipe(new NamespacedKey(plugin, "hyper_golden_apple"));
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var p = event.getPlayer();
        p.discoverRecipe(new NamespacedKey(plugin, "super_golden_apple"));
        p.discoverRecipe(new NamespacedKey(plugin, "hyper_golden_apple"));
    }

    @EventHandler
    public void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        final var player = event.getPlayer();
        final var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        final var item = event.getItem();

        if (item.getType() != Material.GOLDEN_APPLE) {
            return;
        }

        if (item.getItemMeta() == null || !item.getItemMeta().hasCustomModelData()) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(item.getItemMeta().getCustomModelData() == 19) {
                player.removePotionEffect(PotionEffectType.ABSORPTION);
                player.removePotionEffect(PotionEffectType.REGENERATION);

                player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (20 * 120), 1));
            }

            if(item.getItemMeta().getCustomModelData() == 20) {
                player.removePotionEffect(PotionEffectType.ABSORPTION);
                player.removePotionEffect(PotionEffectType.REGENERATION);

                if(attribute == null) {
                    event.setCancelled(true);
                    return;
                }
                if(attribute.getBaseValue() >= 80.0D) {
                    event.setCancelled(true);
                    return;
                }

                attribute.setBaseValue(attribute.getBaseValue() + 4.0D);

                player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
            }

        }, 1L);
    }
}