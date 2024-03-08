package io.github.wickeddroid.plugin.command.staff;

import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Sender;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Command(names = "world")
public class CommandWorld implements CommandClass {

  @Command(names = "tp")
  public void tp(
          final @Sender Player sender,
          final World world
  ) {
    sender.teleport(world.getSpawnLocation());
  }
}
