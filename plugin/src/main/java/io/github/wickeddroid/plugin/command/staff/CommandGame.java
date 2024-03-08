package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.world.Worlds;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Named;
import team.unnamed.commandflow.annotated.annotation.OptArg;
import team.unnamed.commandflow.annotated.annotation.SubCommandClasses;
import team.unnamed.commandflow.annotated.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.InjectAll;

import java.io.IOException;

@InjectAll
@Command(names = "game")
@SubCommandClasses({ CommandTime.class })
public class CommandGame implements CommandClass {
  private Worlds worlds;
  private Plugin plugin;
  private UhcGame uhcGame;
  private UhcTeamRegistry uhcTeamRegistry;
  private UhcGameManager uhcGameManager;
  private Backup backup;
  private MessageHandler messageHandler;
  private Messages messages;

  @Command(names = "host")
  public void host(final @Sender Player sender) {
    this.uhcGame.setHost(sender.getName());
  }

  @Command(names = "start")
  public void start(final @Sender Player sender, @Named("scatter") boolean tp, @OptArg(value = "uhc_world") @Named("world") World world) {
    this.uhcGameManager.startGame(sender, tp, world == null ? Bukkit.getWorld(worlds.worldName()) : world);
  }

  @Command(names = "backup")
  public void backup(final @Sender Player sender) {
    try {
      backup.save();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Command(names = "players-size")
  public void playersSize(final @Sender Player sender, final int size) {
    if(size <= uhcGame.getTeamSize()) { return; }

    this.uhcGame.setPlayersSize(size);
  }


}


