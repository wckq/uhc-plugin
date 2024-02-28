package io.github.wickeddroid.plugin.module;

import io.github.wickeddroid.api.event.UhcEventManager;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.DefaultUhcEventManager;
import io.github.wickeddroid.plugin.game.DefaultUhcGame;
import team.unnamed.inject.AbstractModule;

public class GameModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UhcGame.class)
            .to(DefaultUhcGame.class)
            .singleton();

    bind(UhcEventManager.class)
            .to(DefaultUhcEventManager.class)
            .singleton();
  }
}
