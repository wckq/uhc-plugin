package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.plugin.game.UhcGameHandler;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "uhc")
public class UhcCommand implements CommandClass {
  @Inject
  private UhcGameHandler uhcGameHandler;

  @Command(names = "create")
  public void create(final @Sender Player sender) {
    this.uhcGameHandler.startGame(sender);
  }
}
