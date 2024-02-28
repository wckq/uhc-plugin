package io.github.wickeddroid.api.util;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsProvider {

    public static final boolean usesLuckPerms = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;

    public static String getPrefix(Player player) {
        if(!usesLuckPerms) { return ""; }

        try {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

            if(provider == null) { return ""; }

            LuckPerms api = net.luckperms.api.LuckPermsProvider.get();

            var user = api.getUserManager().getUser(player.getUniqueId());

            var prefix = user.getCachedData().getMetaData().getPrefix();

            return prefix != null ? prefix : "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSuffix(Player player) {
        if(!usesLuckPerms) { return ""; }

        try {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

            if(provider == null) { return ""; }

            LuckPerms api = net.luckperms.api.LuckPermsProvider.get();

            var user = api.getUserManager().getUser(player.getUniqueId());

            var suffix = user.getCachedData().getMetaData().getSuffix();

            return suffix != null ? suffix : "";
        } catch (Exception e) {
            return "";
        }
    }
}
