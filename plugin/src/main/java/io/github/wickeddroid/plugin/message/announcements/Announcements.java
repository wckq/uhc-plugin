package io.github.wickeddroid.plugin.message.announcements;

import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
public class Announcements {

  private List<GameAnnouncement> announcements = List.of(
          new GameAnnouncement(
                  Title.title(
                          MessageUtil.parseStringToComponent("<red>¡UHC Iniciado!"),
                          MessageUtil.parseStringToComponent("<aqua>Recolecta recursos para poder pelear."),
                          Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(4), Duration.ofSeconds(3))
                  ),
                  "0",
                  Sound.sound(
                          Key.key("ui.toast.challenge_complete"),
                          Sound.Source.VOICE,
                          1.0f,
                          1.0f
                  ),
                  Collections.emptyList()
          ),
          new GameAnnouncement(
                  Title.title(
                          MessageUtil.parseStringToComponent("<red>El PvP Iniciará pronto"),
                          MessageUtil.parseStringToComponent("<gold>El PvP se activará en 5 minutos."),
                          Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(4), Duration.ofSeconds(1))
                  ),
                  "%pvp%-300",
                  Sound.sound(
                          Key.key("block.note_block.pling"),
                          Sound.Source.VOICE,
                          1.0f,
                          0.5f
                  ),
                  Collections.emptyList()
          ),
          new GameAnnouncement(
                  Title.title(
                          MessageUtil.parseStringToComponent("<red>PvP ON"),
                          MessageUtil.parseStringToComponent("<aqua>Ahora es legal matar y robar a jugadores"),
                          Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(4), Duration.ofSeconds(3))
                  ),
                  "%pvp%",
                  Sound.sound(
                          Key.key("item.shield.break"),
                          Sound.Source.VOICE,
                          2.0f,
                          0.2f
                  ),
                  Collections.emptyList()
          ),
          new GameAnnouncement(
                  Title.title(
                          MessageUtil.parseStringToComponent("<red>¡MEETUP!"),
                          MessageUtil.parseStringToComponent("<gold>Tienes 10 minutos para llegar a 0,0"),
                          Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(4), Duration.ofSeconds(3))
                  ),
                  "%meetup%",
                  Sound.sound(
                          Key.key("entity.wither.spawn"),
                          Sound.Source.VOICE,
                          2.0f,
                          1.0f),
                  Collections.emptyList()
          ),
          new GameAnnouncement(
                  Title.title(
                          MessageUtil.parseStringToComponent("<red>¡El borde se ha cerrado!"),
                          MessageUtil.parseStringToComponent("<gold>Tardará 30 minutos en cerrarse por completo"),
                          Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(4), Duration.ofSeconds(1))
                  ),
                  "%meetup%+%wb-delay%",
                  Sound.sound(
                          Key.key("block.note_block.pling"),
                          Sound.Source.VOICE,
                          1.0f,
                          0.5f
                  ),
                  Collections.emptyList()
          )
  );

  public @NonNull List<GameAnnouncement> announcements() { return this.announcements; }


}
