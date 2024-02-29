package io.github.wickeddroid.api.util.profile;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface ProfileModifier {

    boolean setSkin(Player victim, List<Player> viewers, OfflinePlayer skin) throws ClassNotFoundException;
    boolean clearSkin(Player victim) throws ClassNotFoundException;

    boolean setName(Player victim, List<Player> viewers, String name) throws ClassNotFoundException;
    boolean clearName(Player victim) throws ClassNotFoundException;

}
