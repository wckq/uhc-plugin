package io.github.wickeddroid.plugin.game;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Game {
    private boolean starterInvulnerability = true;
    private int invulnerabilityDuration = 10;
    private boolean ironmanEnabled = true;
    private boolean papermanEnabled = true;
    private boolean spectatorsEnabled = false;
    private boolean laterScatterIronman = false;
    private boolean initialBoat = true;
    private boolean useExperimentalScatter = false;
    private boolean banRegenerationPotion = true;
    private boolean banAmplifiedStrengthPotion = true;
    private boolean useLoginSecurityPrevention = false;
    private Sound playerDeathSound = Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.PLAYER, 1.0F, 1.0F);

    public boolean starterInvulnerability() { return this.starterInvulnerability; }
    public int invulnerabilityDuration() { return this.invulnerabilityDuration; }
    public boolean ironmanEnabled() { return this.ironmanEnabled; }
    public boolean papermanEnabled() { return this.papermanEnabled; }
    public boolean spectatorsEnabled() { return this.spectatorsEnabled; }
    public boolean laterScatterIronman() { return this.laterScatterIronman; }
    public boolean initialBoat() { return this.initialBoat; }
    public boolean useExperimentalScatter() { return this.useExperimentalScatter; }
    public boolean banRegenerationPotion() { return this.banRegenerationPotion; }
    public boolean banAmplifiedStrengthPotion() { return this.banAmplifiedStrengthPotion; }
    public boolean useLoginSecurityPrevention() { return this.useLoginSecurityPrevention; }
    public @NonNull Sound playerDeathSound() { return this.playerDeathSound; }

    private PlayerList playerList = new PlayerList();
    private AncientCityNerf ancientCityNerf = new AncientCityNerf();
    private Episodes episodes = new Episodes();

    public @NonNull PlayerList playerList() { return this.playerList; }
    public @NonNull AncientCityNerf ancientCityNerf() { return this.ancientCityNerf; }
    public @NonNull Episodes episodes() { return this.episodes; }


    @ConfigSerializable
    public static class PlayerList {
        private String header = "<br>";
        private String footer = "<br><green>Plugin by <gold>@wckq_ @mragus5534<br><aqua>https://github.com/wckq/uhc-plugin<br>";

        public @NonNull String header() { return this.header; }
        public @NonNull String footer() { return this.footer; }
    }

    @ConfigSerializable
    public static class AncientCityNerf {
        private boolean enabled = false;
        private boolean replaceItemOnChange = true;
        private boolean enchantedGoldenAppleEnabled = true;
        private int enchantedGoldenAppleChance = 20;
        private boolean regenerationPotionEnabled = true;
        private int regenerationPotionChance = 50;
        private boolean diamondArmorEnabled = true;
        private int diamondArmorChance = 33;

        public boolean enabled() { return this.enabled; }
        public boolean replaceItemOnChange() { return this.replaceItemOnChange; }
        public boolean enchantedGoldenAppleEnabled() { return this.enchantedGoldenAppleEnabled; }
        public int enchantedGoldenAppleChance() { return this.enchantedGoldenAppleChance; }
        public boolean regenerationPotionEnabled() { return this.regenerationPotionEnabled; }
        public int regenerationPotionChance() { return this.regenerationPotionChance; }
        public boolean diamondArmorEnabled() { return this.diamondArmorEnabled; }
        public int diamondArmorChance() { return this.diamondArmorChance; }
    }

    @ConfigSerializable
    public static class Episodes {
        private boolean enabled = false;
        private boolean reversedTimer = true;
        private long episodeDurationTicks = 24000L;
        private int finalEpisode = 10;

        public boolean enabled() { return this.enabled; }
        public boolean reversedTimer() { return this.reversedTimer; }
        public long episodeDurationTicks() { return this.episodeDurationTicks; }
        public int finalEpisode() { return this.finalEpisode; }
    }
}
