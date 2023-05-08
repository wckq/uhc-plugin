package io.github.wickeddroid.plugin.message.title;

import io.github.wickeddroid.plugin.util.MessageUtil;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Titles {
  private Title pvpTitle = Title.title(
          MessageUtil.parseStringToComponent("¡El pvp ha comenzado!"),
          MessageUtil.parseStringToComponent("El pvp ha comenzado, ten cuidado.")
  );

  public @NonNull Title pvpTitle() {
    return this.pvpTitle;
  }
}
