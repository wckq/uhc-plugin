package io.github.wickeddroid.plugin.module;

import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.loader.CommandLoader;
import io.github.wickeddroid.plugin.loader.DefaultLoader;
import io.github.wickeddroid.plugin.loader.ListenerLoader;
import io.github.wickeddroid.plugin.loader.WorldLoader;
import team.unnamed.inject.AbstractModule;

public class LoaderModule extends AbstractModule {
  @Override
  protected void configure() {
    bindLoader("default-loader", DefaultLoader.class);
    bindLoader("command-loader", CommandLoader.class);
    bindLoader("listener-loader", ListenerLoader.class);
    bindLoader("world-loader", WorldLoader.class);
  }

  private void bindLoader(
          final String name,
          final Class<? extends Loader> clazz
  ) {
    bind(Loader.class)
            .named(name)
            .to(clazz);
  }
}
