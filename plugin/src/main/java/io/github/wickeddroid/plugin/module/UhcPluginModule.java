package io.github.wickeddroid.plugin.module;

import io.github.wickeddroid.api.util.glowing.GlowingEntities;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.AbstractModule;

public class UhcPluginModule extends AbstractModule {

  private final Plugin plugin;

  public UhcPluginModule(final Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(GlowingEntities.class).toInstance(new GlowingEntities(plugin));

    install(new GameModule());
    install(new LoaderModule());

    install(new ScoreboardModule());

    install(new ConfigurationModule(plugin));
  }
}
