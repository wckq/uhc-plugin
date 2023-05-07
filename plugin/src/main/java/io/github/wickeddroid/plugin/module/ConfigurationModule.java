package io.github.wickeddroid.plugin.module;

import io.github.wickeddroid.plugin.configuration.JsonConfigurationBuilder;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.scoreboard.Scoreboard;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.ConfigurateException;
import team.unnamed.inject.AbstractModule;

import java.nio.file.Path;

public class ConfigurationModule extends AbstractModule {
  private final Path path;

  public ConfigurationModule(final Plugin plugin) {
    this.path = Path.of(plugin.getDataFolder().getAbsolutePath());
  }

  @Override
  protected void configure() {
    try {
      bind(Messages.class)
              .toInstance(JsonConfigurationBuilder
                      .load(Messages.class, path, "messages")
                      .get()
              );

      bind(Scoreboard.class)
              .toInstance(JsonConfigurationBuilder
                      .load(Scoreboard.class, path, "scoreboards")
                      .get()
              );
    } catch (ConfigurateException e) {
      throw new RuntimeException(e);
    }
  }
}
