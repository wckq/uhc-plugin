package io.github.wickeddroid.plugin.scenario.scenarios;

import com.destroystokyo.paper.MaterialTags;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RegisteredScenario
@Scenario(
        name = "CutClean",
        key = "cut_clean",
        description = {""},
        material = Material.IRON_INGOT
)
public class CutCleanScenario extends ListenerScenario {

  @Inject
  private UhcGame uhcGame;

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    if(uhcGame.getUhcGameState() == UhcGameState.WAITING) { return; }

    Map<Material, Material> dropReplacements = new java.util.HashMap<>(Map.of(
            Material.RAW_IRON, Material.IRON_INGOT,
            Material.RAW_COPPER, Material.COPPER_INGOT,
            Material.ANCIENT_DEBRIS, Material.NETHERITE_SCRAP,
            Material.RAW_GOLD, Material.GOLD_INGOT,
            Material.GRAVEL, Material.FLINT,
            Material.SAND, Material.GLASS,
            Material.RED_SAND, Material.GLASS,
            Material.RAW_IRON_BLOCK, Material.IRON_BLOCK
    ));

    var block = event.getBlock();

    var drops = block.getDrops(event.getPlayer().getItemInHand());

    var dropsMaterial = drops.stream().collect(Collectors.toMap(ItemStack::getType, itemStack -> itemStack));

    List<ItemStack> newDrops = new ArrayList<>();
    dropReplacements.forEach((from, to) -> {

      if(dropsMaterial.containsKey(from)) {
        var itemStack = dropsMaterial.get(from);
        newDrops.add(new ItemStack(to, itemStack.getAmount()));
      }
    });

    if(!newDrops.isEmpty()) {
      event.setDropItems(false);

      ExperienceOrb orb = block.getWorld().spawn(block.getLocation(), ExperienceOrb.class);
      orb.setExperience(event.getExpToDrop());
      newDrops.forEach(d ->    block.getWorld().dropItemNaturally(block.getLocation(), d));
    }

  }

  @EventHandler
  public void onEntityDeath(final EntityDeathEvent event) {
    if(event.getEntity().getType() == EntityType.PLAYER) { return; }
    if(uhcGame.getUhcGameState() == UhcGameState.WAITING) { return; }

    final var drops = event.getDrops();

    Map<Material, Material> dropReplacements = Map.of(
            Material.PORKCHOP, Material.COOKED_PORKCHOP,
            Material.BEEF, Material.COOKED_BEEF,
            Material.MUTTON, Material.COOKED_MUTTON,
            Material.CHICKEN, Material.COOKED_CHICKEN,
            Material.RABBIT, Material.COOKED_RABBIT
    );

    var dropsMaterial = drops.stream().collect(Collectors.toMap(ItemStack::getType, itemStack -> itemStack));

    dropReplacements.forEach((from, to) -> {


      if(dropsMaterial.containsKey(from)) {
        var itemStack = dropsMaterial.get(from);

        drops.remove(itemStack);
        drops.add(new ItemStack(to, itemStack.getAmount()));
      }
    });
  }
}
