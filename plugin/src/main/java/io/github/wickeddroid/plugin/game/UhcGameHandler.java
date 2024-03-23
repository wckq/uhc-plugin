package io.github.wickeddroid.plugin.game;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.message.announcements.Announcements;
import team.unnamed.commandflow.annotated.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

@InjectAll
public class UhcGameHandler {

  private Announcements announcements;
  private UhcGame uhcGame;
  private Game game;
  private Messages messages;
  private MessageHandler messageHandler;

  public void changePvp(final boolean pvp) {
    for (final var world : Bukkit.getWorlds()) {
      if (world == null) {
        continue;
      }

      world.setPVP(pvp);
    }

    this.uhcGame.setUhcGameState(UhcGameState.PVP);
    this.uhcGame.setPvp(pvp);
  }

  public void changeTimePvp(
          final @Sender Player sender,
          final int time
  ) {
    if (time < 0) {
      this.messageHandler.send(sender, this.messages.staff().invalidTime());
      return;
    }

    if(game.episodes().enabled() && time > game.episodes().finalEpisode() * (game.episodes().episodeDurationTicks()/20)) {
      this.messageHandler.send(sender, this.messages.staff().timeHigherThanEpisodes());
      return;
    }

    this.uhcGame.setTimeForPvp(time);
    this.messageHandler.send(sender, this.messages.staff().changePvpTime(), String.valueOf(time));
  }

  public void changeMeetupPvp(
          final @Sender Player sender,
          final int time
  ) {
    if (time < 0) {
      this.messageHandler.send(sender, this.messages.staff().invalidTime());
      return;
    }

    if(game.episodes().enabled() && time > game.episodes().finalEpisode() * (game.episodes().episodeDurationTicks()/20)) {
      this.messageHandler.send(sender, this.messages.staff().timeHigherThanEpisodes());
      return;
    }

    this.uhcGame.setTimeForMeetup(time);
    this.messageHandler.send(sender, this.messages.staff().changeMeetupTime(), String.valueOf(time));
  }
}
