package io.github.wickeddroid.plugin.configuration.serializer;

import io.github.wickeddroid.plugin.message.announcements.GameAnnouncement;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum GameAnnouncementSerializer implements TypeSerializer<GameAnnouncement> {
    INSTANCE;

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }

        return source.node(path);
    }

    @Override
    public GameAnnouncement deserialize(Type type, ConfigurationNode node) throws SerializationException {
        var title = nonVirtualNode(node, "title").get(TypeToken.get(Title.class));
        var timeNode = nonVirtualNode(node, "time");

        if(timeNode.empty()) {
            throw new IllegalArgumentException("Time is not provided");
        }

        var sound = nonVirtualNode(node, "sound").get(TypeToken.get(Sound.class));
        var scenarios = nonVirtualNode(node, "scenarios").getList(String.class);

        return new GameAnnouncement(title, timeNode.getString(), sound, scenarios);
    }

    @Override
    public void serialize(Type type, @Nullable GameAnnouncement announcement, ConfigurationNode node) throws SerializationException {
        if(announcement == null)  {
            node.raw(null);
            return;
        }

        node.node("title").set(Title.class, announcement.title());
        node.node("time").set(Objects.requireNonNull(announcement.time()));
        node.node("sound").set(Sound.class, announcement.sound());
        node.node("scenarios").setList(String.class, announcement.scenarios());
    }
}
