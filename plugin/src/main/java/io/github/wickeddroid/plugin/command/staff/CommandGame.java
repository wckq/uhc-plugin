package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.runnable.ScatterRunnable;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.LocationUtils;
import io.github.wickeddroid.plugin.world.Worlds;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

import java.util.Random;

@Command(names = "game")
public class CommandGame implements CommandClass {
  @Inject private Worlds worlds;
  @Inject private Plugin plugin;
  @Inject private UhcGame uhcGame;
  @Inject private UhcTeamRegistry uhcTeamRegistry;
  @Inject private UhcGameHandler uhcGameHandler;

  private static final Random RANDOM = new Random();

  @Command(names = "host")
  public void host(final @Sender Player sender) {
    this.uhcGame.setHost(sender.getName());
  }

  @Command(names = "start")
  public void start(final @Sender Player sender) {
    this.uhcGameHandler.startGame(sender);
  }
}
