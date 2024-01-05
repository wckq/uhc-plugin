package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "time")
public class CommandTime implements CommandClass {

  @Inject
  private UhcGameHandler uhcGameHandler;
  @Inject
  private UhcGame uhcGame;

  @Command(names = "change-pvp")
  public void changePvp(
          final @Sender Player sender,
          final int time
  ) {
    this.uhcGameHandler.changeTimePvp(sender, time);
  }

  @Command(names = "change-meetup")
  public void changeMeetup(
          final @Sender Player sender,
          final int time
  ) {
    this.uhcGameHandler.changeMeetupPvp(sender, time);
  }

  @Command(
          names = "set"
  )
  public void set(
          final @Sender Player sender,
          final int time
  ) {
    this.uhcGame.setStartTime(uhcGame.getStartTime() - (time * 1000L));
  }
}
