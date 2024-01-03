package io.github.wickeddroid.plugin.message.title;

import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Titles {

  private Title gameStart = Title.title(
          MessageUtil.parseStringToComponent("<red>¡UHC Iniciado!"),
          MessageUtil.parseStringToComponent("<aqua>Recolecta recursos para poder pelear.")
  );

  private Title pvpTitle = Title.title(
          MessageUtil.parseStringToComponent("<red>PVP ON"),
          MessageUtil.parseStringToComponent("<aqua>Ahora es legal matar y robar a jugadores")
  );

  private Title meetupTitle = Title.title(
          MessageUtil.parseStringToComponent("<red>MEETUP"),
          MessageUtil.parseStringToComponent("<gold>Dirigete a 0,0 para la batalla final.")
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
