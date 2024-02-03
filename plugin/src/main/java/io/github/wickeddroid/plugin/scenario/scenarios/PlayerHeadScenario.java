package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.util.item.ItemBuilder;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;

import java.util.LinkedList;
import java.util.List;

@RegisteredScenario
@Scenario(
        name = "Player Head",
        key = "player_head",
        description = {
                "<gray>- Los <aqua>Jugadores <gray>sueltan su cabeza.",
                "<gray>- Puede craftearse en <gold>Golden Head<gray>."
        },
        material = Material.PLAYER_HEAD,
        supportsOptions = true
)
public class PlayerHeadScenario extends ListenerScenario {

    @Inject private UhcGame uhcGame;
    @Inject private UhcPlayerRegistry uhcPlayerRegistry;
    @Inject private Plugin plugin;

    public enum DropForm {
        DROP,
        GRAVE
    }

    @ScenarioOption(optionName = "Forma", dynamicValue = "form")
    private LinkedList<OptionValue<DropForm>> formValue = new LinkedList<>(
            List.of(
                    Option.buildValue(DropForm.DROP, "Drop"),
                    Option.buildValue(DropForm.GRAVE, "Tumba")
            )
    );
    private DropForm form;

    @ScenarioOption(optionName = "Bloque de Tumba", dynamicValue = "graveBlock")
    private LinkedList<OptionValue<Material>> blockValue = new LinkedList<>(
            List.of(
                    Option.buildValue(Material.BEDROCK, "Bedrock"),
                    Option.buildValue(Material.GOLD_BLOCK, "Bloque de Oro"),
                    Option.buildValue(Material.EMERALD_BLOCK, "Bloque de Esmeralda"),
                    Option.buildValue(Material.IRON_BLOCK, "Bloque de Hierro")
            )
    );
    private Material graveBlock;

    @ScenarioOption(optionName = "Wither Head", dynamicValue = "witherHead")
    private LinkedList<OptionValue<Boolean>> witherHeadValue = new LinkedList<>(
            List.of(
                    Option.buildValue(false, "Deshabilitado"),
                    Option.buildValue(true, "Habilitado")
            )
    );
    private Boolean witherHead;

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        final var goldenHead = ItemBuilder
                .newBuilder(Material.GOLDEN_APPLE)
                .name(MessageUtil.parseStringToComponent("<yellow>Golden Head").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .modelData(28)
                .build();

        final var goldenHeadRecipe = new ShapedRecipe(new NamespacedKey(plugin, "golden_head"), goldenHead);

        goldenHeadRecipe.shape("GGG", "GHG", "GGG");
        goldenHeadRecipe.setIngredient('G', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT));
        goldenHeadRecipe.setIngredient('H', new RecipeChoice.MaterialChoice(Material.PLAYER_HEAD));

        Bukkit.addRecipe(goldenHeadRecipe);

        final var witherHeadItem = ItemBuilder
                .newBuilder(Material.GOLDEN_APPLE)
                .name(MessageUtil.parseStringToComponent("<dark_gray>Wither Head").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .modelData(28)
                .build();

        final var witherHeadRecipe = new ShapedRecipe(new NamespacedKey(plugin, "wither_head"), witherHeadItem);

        witherHeadRecipe.shape("GGG", "GHG", "GGG");
        witherHeadRecipe.setIngredient('G', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT));
        witherHeadRecipe.setIngredient('H', new RecipeChoice.MaterialChoice(Material.WITHER_SKELETON_SKULL));

        if(witherHead) {
            Bukkit.addRecipe(witherHeadRecipe);
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.discoverRecipe(goldenHeadRecipe.getKey());

            if(witherHead) { p.discoverRecipe(witherHeadRecipe.getKey()); }
        });
    }

    @EventHandler
    public void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        final var player = event.getPlayer();
        final var item = event.getItem();

        if (item.getType() != Material.GOLDEN_APPLE) {
            return;
        }

        if (item.getItemMeta() == null || !item.getItemMeta().hasCustomModelData()) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (item.getItemMeta().getCustomModelData() == 28) {
                player.removePotionEffect(PotionEffectType.ABSORPTION);
                player.removePotionEffect(PotionEffectType.REGENERATION);

                player.addPotionEffect(PotionEffectType.REGENERATION.createEffect(200, 1));
                player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(2400, 1));
            }
        }, 1L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final var player = event.getPlayer();

        final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(player.getName());

        if (this.uhcGame == null) {
            return;
        }

        if(uhcGame.getUhcGameState() == UhcGameState.FINISH || uhcGame.getUhcGameState() == UhcGameState.WAITING) { return; }

        if (uhcPlayer == null) {
            return;
        }

        if(form == DropForm.DROP) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), ItemBuilder.newBuilder(Material.PLAYER_HEAD).skull(player).build());
        } else {
            var loc = player.getLocation();
            var world = loc.getWorld();

            world.getBlockAt((int) loc.getX(), loc.getBlockY()-1, (int) loc.getZ()).setType(graveBlock);
            world.getBlockAt((int) loc.getX(), loc.getBlockY(), (int) loc.getZ()).setType(Material.NETHER_BRICK_FENCE);
            var block = world.getBlockAt((int) loc.getX(), loc.getBlockY()+1, (int) loc.getZ());

            block.setType(Material.PLAYER_HEAD);

            var skull = (Skull)block.getState();
            skull.setType(Material.PLAYER_HEAD);
            skull.setOwningPlayer(player);
            skull.update();
        }
    }
}
