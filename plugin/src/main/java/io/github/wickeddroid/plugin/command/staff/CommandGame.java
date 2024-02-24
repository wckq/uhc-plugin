package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.backup.Backup;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.world.Worlds;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
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
  public void start(final @Sender Player sender, @Named("scatter") boolean tp) {
    this.uhcGameManager.startGame(sender, tp);
  }

  @Command(names = "backup")
  public void backup(final @Sender Player sender) {
    try {
      backup.save();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Command(names = "apple-rate")
  public void appleRate(final @Sender Player sender, final int appleRate) {
    this.uhcGame.setAppleRate(appleRate);
  }

  @Command(names = "players-size")
  public void playersSize(final @Sender Player sender, final int size) {
    if(size <= uhcGame.getTeamSize()) { return; }

    this.uhcGame.setPlayersSize(size);
  }

  @Command(names = "final-heal-time")
  public void finalHealTime(final @Sender Player sender, final int time) {
    this.uhcGame.setTimeForFinalHeal(time);
    messageHandler.send(sender, messages.staff().changeFinalHealTime(), String.valueOf(time));
  }

  @Command(names = "final-heal-amplifier")
  public void finalHealAmplifier(final @Sender Player sender, final int amplifier) {
    if(amplifier < 0 || amplifier > 15) {
      messageHandler.send(sender, messages.staff().invalidAmplifier(), "0", "15");
      return;
    }

    this.uhcGame.setFinalHealAmplifier(amplifier);
    messageHandler.send(sender, messages.staff().changeFinalHealAmplifier(), String.valueOf(amplifier));
  }

  @Command(names = "final-resistance-time")
  public void finalResistanceTime(final @Sender Player sender, final int time) {
    this.uhcGame.setTimeForFinalResistance(time);
    messageHandler.send(sender, messages.staff().changeFinalResistanceTime(), String.valueOf(time));
  }

  @Command(names = "final-resistance-amplifier")
  public void finalResistanceAmplifier(final @Sender Player sender, final int amplifier) {
    if(amplifier < 0 || amplifier > 5) {
      messageHandler.send(sender, messages.staff().invalidAmplifier(), "0", "5");
      return;
    }

    this.uhcGame.setFinalResistanceAmplifier(amplifier);
    messageHandler.send(sender, messages.staff().changeFinalResistanceAmplifier(), String.valueOf(amplifier));
  }

}


