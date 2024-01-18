package io.github.wickeddroid.plugin;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.module.UhcPluginModule;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import io.github.wickeddroid.plugin.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mariuszgromada.math.mxparser.License;
import team.unnamed.inject.Inject;
import team.unnamed.inject.InjectIgnore;
import team.unnamed.inject.Injector;
import team.unnamed.inject.Named;

import java.util.regex.Pattern;

public class UhcPlugin extends JavaPlugin {

  @Inject @Named("default-loader")
  private Loader loader;

  @Override
  public void onEnable() {
    License.iConfirmNonCommercialUse("uhcplugin");

    Injector.create(new UhcPluginModule(this))
            .injectMembers(this);



  if(PluginUtil.versionNumber < 19 || (PluginUtil.versionNumber == 19 && PluginUtil.patchNumber < 2)) {
    throw new ExceptionInInitializerError("Plugin doesn't support versions older than 1.19");
  }

  if((PluginUtil.versionNumber == 20 && PluginUtil.patchNumber > 2) || PluginUtil.versionNumber > 20) {
    throw new ExceptionInInitializerError("Due to new Scoreboard packets, this version isn't supported yet");
  }

    this.loader.load();
  }

  @Override
  public void onDisable() {
    this.loader.unload();
  }
}
