package io.github.wickeddroid.plugin.loader;

import io.github.wickeddroid.api.loader.Loader;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

public class DefaultLoader implements Loader {
  @Inject @Named("command-loader")
  private Loader commandLoader;

  @Inject @Named("listener-loader")
  private Loader listenerLoader;

  @Override
  public void load() {
    this.commandLoader.load();
    this.listenerLoader.load();
  }
}
