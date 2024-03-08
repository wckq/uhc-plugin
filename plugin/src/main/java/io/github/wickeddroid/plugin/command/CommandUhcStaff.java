package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.plugin.command.staff.*;
import io.github.wickeddroid.plugin.command.staff.CommandTeam;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.player.UhcPlayerHandler;
import io.github.wickeddroid.plugin.world.Worlds;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

@Command(names = "uhc-staff", permission = "uhc.uhc-staff")
@SubCommandClasses({ CommandGame.class, CommandTeam.class, CommandWorld.class, CommandScenario.class, CommandSetting.class})
public class CommandUhcStaff implements CommandClass {
  @Inject private UhcGameManager uhcGameManager;
  @Inject private Worlds worlds;
  @Inject
  private Plugin plugin;

  @Command(names = "later-scatter")
  public void scatter(
          final @Sender Player sender,
          final Player target,
          @OptArg World world
          ) {
    try {
      this.uhcGameManager.scatterPlayer(target, true, world == null ? Bukkit.getWorld(worlds.worldName()) : world);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
