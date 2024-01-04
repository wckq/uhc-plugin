package io.github.wickeddroid.plugin;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.module.UhcPluginModule;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mariuszgromada.math.mxparser.License;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectIgnore;
import team.unnamed.inject.Injector;
import team.unnamed.inject.Named;

public class UhcPlugin extends JavaPlugin {

  @Inject @Named("default-loader")
  private Loader loader;

  @Override
  public void onEnable() {
    License.iConfirmNonCommercialUse("uhcplugin");

    Injector.create(new UhcPluginModule(this))
            .injectMembers(this);


    this.loader.load();
  }

  @Override
  public void onDisable() {
    this.loader.unload();
  }
}
