package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.plugin.command.staff.CommandGame;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "uhc-staff", permission = "uhc.uhc-staff")
@SubCommandClasses({ CommandGame.class })
public class CommandUhcStaff implements CommandClass {

  @Inject private UhcGameHandler uhcGameHandler;

  @Command(names = "create-uhc")
  public void createUhc(final @Sender Player sender) {
    this.uhcGameHandler.changePvp(true);
  }
}
