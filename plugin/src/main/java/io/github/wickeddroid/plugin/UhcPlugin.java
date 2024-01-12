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

import java.util.regex.Pattern;

public class UhcPlugin extends JavaPlugin {

  @Inject @Named("default-loader")
  private Loader loader;

  @Override
  public void onEnable() {
    License.iConfirmNonCommercialUse("uhcplugin");

    Injector.create(new UhcPluginModule(this))
            .injectMembers(this);


    var version = Bukkit.getServer().getClass().getName().split(Pattern.quote("."))[3];
    var str = version.split(Pattern.quote("_"));
    var versionNumber = Byte.parseByte(str[1]);
    var patchNumber = Byte.parseByte(str[2].substring(1));

  if(versionNumber < 19) {
    throw new ExceptionInInitializerError("Plugin doesn't support versions older than 1.19");
  }

  if(versionNumber >= 20 && patchNumber > 2) {
    throw new ExceptionInInitializerError("Due to new Scoreboard packets, this version isn't supported yet");
  }

    this.loader.load();
  }

  @Override
  public void onDisable() {
    this.loader.unload();
  }
}
