package io.github.wickeddroid.plugin.game;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Game {

    private boolean starterInvulnerability = true;
    private int invulnerabilityDuration = 10;
    private boolean ironmanEnabled = true;
    private boolean papermanEnabled = true;
    private boolean spectatorsEnabled = false;
    private boolean initialBoat = true;

    public boolean starterInvulnerability() { return this.starterInvulnerability; }
    public int invulnerabilityDuration() { return this.invulnerabilityDuration; }
    public boolean ironmanEnabled() { return this.ironmanEnabled; }
    public boolean papermanEnabled() { return this.papermanEnabled; }
    public boolean spectatorsEnabled() { return this.spectatorsEnabled; }
    public boolean initialBoat() { return this.initialBoat; }

    private PlayerList playerList = new PlayerList();

    public @NonNull PlayerList playerList() { return this.playerList; }


    @ConfigSerializable
    public static class PlayerList {
        private String header = "<br>";
        private String footer = "<br><green>Plugin by <gold>@wckq_ @mragus5534<br><aqua>https://github.com/wckq/uhc-plugin<br>";

        public @NonNull String header() { return this.header; }
        public @NonNull String footer() { return this.footer; }
    }
}
