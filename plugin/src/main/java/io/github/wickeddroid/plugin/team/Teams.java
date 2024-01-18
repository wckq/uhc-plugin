package io.github.wickeddroid.plugin.team;

import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.List;

@ConfigSerializable
public class Teams {

    private Customization customization = new Customization();
    private String teamPrefix = "[Team <leader>] | ";
    private String defaultName = "%s team";
    private String fastChatTogglePrefix = "!";
    private List<String> colors = Arrays.stream(new NamedTextColor[]{
            NamedTextColor.AQUA,
            NamedTextColor.BLUE,
            NamedTextColor.DARK_AQUA,
            NamedTextColor.DARK_BLUE,
            NamedTextColor.DARK_GRAY,
            NamedTextColor.DARK_RED,
            NamedTextColor.YELLOW,
            NamedTextColor.LIGHT_PURPLE,
            NamedTextColor.RED,
            NamedTextColor.GREEN,
            NamedTextColor.GRAY
    }).map(NamedTextColor::toString).toList();

    private boolean forceSequentiallyColors = true;
    private boolean friendlyFire = false;
    public @NonNull String defaultName() { return this.defaultName; }

    public @NonNull String teamPrefix() { return this.teamPrefix; }
    public @NonNull List<String> colors() { return this.colors; }
    public @NonNull String fastChatTogglePrefix() { return this.fastChatTogglePrefix; }
    public boolean forceSequentiallyColors() { return this.forceSequentiallyColors; }
    public boolean friendlyFire() { return this.friendlyFire; }
    public Customization customization() { return this.customization; }

    @ConfigSerializable
    public static class Customization {
        private boolean allowCustomPrefix = false;
        private boolean allowCustomName = true;
        private int maxNameLength = 22;
        private int maxPrefixLength = 16;

        public boolean allowCustomPrefix() { return this.allowCustomPrefix; }
        public boolean allowCustomName() { return this.allowCustomName; }
        public int maxNameLength() { return this.maxNameLength; }
        public int maxPrefixLength() { return this.maxPrefixLength; }
    }

}
