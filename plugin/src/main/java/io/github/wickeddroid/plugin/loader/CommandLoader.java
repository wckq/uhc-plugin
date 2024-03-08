package io.github.wickeddroid.plugin.loader;

import bukkit.BukkitMapCommandManager;
import io.github.wickeddroid.api.loader.Loader;
import io.github.wickeddroid.plugin.command.CommandPlayer;
import io.github.wickeddroid.plugin.command.CommandTeam;
import io.github.wickeddroid.plugin.command.CommandUhcStaff;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import team.unnamed.commandflow.annotated.AnnotatedCommandTreeBuilder;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.builder.AnnotatedCommandBuilder;
import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.annotated.part.defaults.DefaultsModule;

import team.unnamed.commandflow.bukkit.factory.BukkitModule;
import team.unnamed.commandflow.command.Command;
import team.unnamed.inject.InjectAll;
import team.unnamed.inject.Injector;

import java.lang.reflect.Field;

@InjectAll
public class CommandLoader implements Loader {

  private Injector injector;

  private CommandPlayer commandPlayer;
  private CommandTeam commandTeam;
  private CommandUhcStaff commandUhcStaff;

  private ScenarioRegistration scenarioRegistration;

  @Override
  public void load() {
    registerCommands(
            commandPlayer,
            commandTeam,
            commandUhcStaff
    );
  }

  private void registerCommands(CommandClass... commandClasses) {


    final var partInjector = PartInjector.create();
    partInjector.install(new DefaultsModule());
    partInjector.install(new BukkitModule());

    final var treeBuilder = AnnotatedCommandTreeBuilder.create(
            AnnotatedCommandBuilder.create(partInjector),
            ((clazz, parent) -> this.injector.getInstance(clazz))
    );
    final var commandManager = new BukkitMapCommandManager("uhc");

    for (final var commandClass : commandClasses) {
      commandManager.registerCommands(treeBuilder.fromClass(commandClass));
    }
  }
}
