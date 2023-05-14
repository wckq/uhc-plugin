package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.listener.custom.PlayerScatteredListener;
import io.github.wickeddroid.plugin.listener.vanilla.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.InjectAll;

@InjectAll
public class ListenerLoader implements Loader {
  private Plugin plugin;
  private PlayerJoinListener playerJoinListener;
  private PlayerQuitListener playerQuitListener;
  private PlayerDeathListener playerDeathListener;
  private AsyncChatListener asyncChatListener;
  private EntityDamageByEntityListener entityDamageByEntityListener;
  private PlayerScatteredListener playerScatteredListener;

  @Override
  public void load() {
    registerListeners(
            playerJoinListener,
            playerQuitListener,
            playerDeathListener,
            asyncChatListener,
            entityDamageByEntityListener,
            playerScatteredListener
    );
  }

  private void registerListeners(Listener... listeners) {
    for (final var listener : listeners) {
      Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
  }
}
