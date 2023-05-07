package io.github.wickeddroid.plugin;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.module.UhcPluginModule;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Injector;
import team.unnamed.inject.Named;

public class UhcPlugin extends JavaPlugin {

  @Inject @Named("default-loader")
  private Loader loader;

  @Override
  public void onEnable() {
    Injector.create(new UhcPluginModule(this))
            .injectMembers(this);

    this.loader.load();
  }
}
