package io.github.wickeddroid.api.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderProvider {
    public static final boolean usesPlaceholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    public static String parse(Audience audience, String content) {
        if(!usesPlaceholderAPI) { return content; }

        try {
            return PlaceholderAPI.setPlaceholders((Player) audience, content);
        } catch (Exception e) {
            return content;
        }
    }

}
