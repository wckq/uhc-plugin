package io.github.wickeddroid.plugin.world;

import io.github.wickeddroid.plugin.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@ConfigSerializable
public class Worlds {

  private Border border = new Border();
  private String seed = "";
  private String worldName = "uhc_world";
  private List<String> blacklist = new ArrayList<>(Collections.singleton("world"));
  private boolean hardcore = true;
  private boolean naturalRegeneration = false;
  private boolean removeWorld = true;
  public @NonNull List<String> blacklist() {
    return this.blacklist;
  }


  public @NonNull String worldName() {
    return this.worldName;
  }

  public @NonNull String seed() {
    return this.seed;
  }

  public boolean naturalRegeneration() {
    return this.naturalRegeneration;
  }

  public boolean hardcore() {
    return this.hardcore;
  }

  public boolean removeWorld() { return this.removeWorld; }
  public Border border() { return this.border; }

  @ConfigSerializable
  public static class Border {
    private int worldBorder = 2000;
    private int meetupWorldBorder = 300;
    private int worldBorderDelay = 600;
    private double initialBorderDamage = 0.0D;
    private double meetupBorderDamage = 0.01D;
    private boolean keepClosingAfterMeetup = false;
    private int worldBorderDelayAfterMeetup = 1200;
    private List<String> worldBorderWorlds = PluginUtil.appendList(Bukkit.getWorlds().stream().map(WorldInfo::getName).filter(w -> !w.equals(Bukkit.getWorlds().get(0).getName())).toList(), "uhc_world");

    public int worldBorder() {
      return this.worldBorder;
    }
    public int meetupWorldBorder() { return this.meetupWorldBorder; }
    public int worldBorderDelay() { return this.worldBorderDelay; }
    public double initialBorderDamage() { return this.initialBorderDamage; }
    public double meetupBorderDamage() { return this.meetupBorderDamage; }
    public boolean keepClosingAfterMeetup() { return this.keepClosingAfterMeetup; }
    public int worldBorderDelayAfterMeetup() { return this.worldBorderDelayAfterMeetup; }
    public @NonNull List<String> worldBorderWorlds() { return this.worldBorderWorlds; }
  }
}
