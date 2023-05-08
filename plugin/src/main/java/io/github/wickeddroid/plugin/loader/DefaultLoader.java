package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.loader.Loader;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

public class DefaultLoader implements Loader {
  @Inject @Named("command-loader")
  private Loader commandLoader;

  @Inject @Named("listener-loader")
  private Loader listenerLoader;

  @Inject @Named("world-loader")
  private Loader worldLoader;

  @Override
  public void load() {
    this.worldLoader.load();
    this.commandLoader.load();
    this.listenerLoader.load();
  }

  @Override
  public void unload() {
    this.worldLoader.unload();
  }
}
