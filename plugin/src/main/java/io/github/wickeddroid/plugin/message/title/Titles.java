package io.github.wickeddroid.plugin.message.title;

import io.github.wickeddroid.plugin.util.MessageUtils;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Titles {
  private Title gameStart = Title.title(
          MessageUtils.parseStringToComponent("¡UHC Iniciado!"),
          MessageUtils.parseStringToComponent("Recolecta recursos para poder pelear.")
  );

  private Title pvpTitle = Title.title(
          MessageUtils.parseStringToComponent("PVP ON"),
          MessageUtils.parseStringToComponent("Ahora es legal matar y robar a jugadores")
  );

  private Title meetupTitle = Title.title(
          MessageUtils.parseStringToComponent("MEETUP"),
          MessageUtils.parseStringToComponent("Dirigete a 0,0 para la batalla final.")
  );

  public @NonNull Title gameStart() {
    return this.gameStart;
  }

  public @NonNull Title pvpTitle() {
    return this.pvpTitle;
  }

  public @NonNull Title meetupTitle() {
    return this.meetupTitle;
  }
}
