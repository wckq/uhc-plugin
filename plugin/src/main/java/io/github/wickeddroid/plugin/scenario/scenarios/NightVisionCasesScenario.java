package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.events.GameStartEvent;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.plugin.event.game.PlayerScatteredEvent;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;

@RegisteredScenario
@Scenario(
        name = "Night Vision Cases",
        key = "nv_cases",
        description = {},
        material = Material.DIAMOND_HELMET
)
public class NightVisionCasesScenario extends ListenerScenario {
  @Inject private Plugin plugin;

  private final int DURATION = PluginUtil.versionNumber == 19 && PluginUtil.patchNumber < 4 ? Integer.MAX_VALUE : -1;

  @EventHandler
  public void onStart(GameStartEvent event) {
    Bukkit.getOnlinePlayers().forEach(p -> {
      p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, DURATION, 0, false, false, false));
    });
  }

  @EventHandler
  public void onEffectRemove(PlayerItemConsumeEvent event) {
    if(event.getItem() == null) { return; }

    if(event.getItem().getType() == Material.MILK_BUCKET) {
      Bukkit.getScheduler().runTaskLater(plugin, ()->event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, DURATION, 0, false, false, false)), 2L);
    }
  }
}
