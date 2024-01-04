package io.github.wickeddroid.plugin.configuration.serializer;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public enum SoundSerializer implements TypeSerializer<Sound> {
    INSTANCE;

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }

        return source.node(path);
    }

    @Override
    public Sound deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final var sound = nonVirtualNode(node, "sound");

        if(sound.empty()) {
            throw new IllegalArgumentException("Sound cannot be null");
        }

        final var source = nonVirtualNode(node, "source");
        var soundSource = source.getString();

        if(source.empty() || soundSource == null) {
           soundSource = Sound.Source.AMBIENT.name();
        }

        final var volumeNode = nonVirtualNode(node, "volume");
        var volume = volumeNode.getFloat();

        if(volumeNode.empty() || volume <= 0.0F) {
            volume = 1.0F;
        }

        final var pitchNode = nonVirtualNode(node, "pitch");
        var pitch = pitchNode.getFloat();

        if(pitchNode.empty() || pitch <= 0.0F) {
            pitch = 1.0F;
        }


        return Sound.sound(
                Key.key(sound.getString()),
                Sound.Source.valueOf(soundSource.toUpperCase()),
                volume,
                pitch

        );
    }

    @Override
    public void serialize(Type type, @Nullable Sound sound, ConfigurationNode node) throws SerializationException {
        if(sound == null) {
            node.raw(null);
            return;
        }

        node.node("sound").set(Objects.requireNonNull(sound).name().key().value());
        node.node("source").set(Objects.requireNonNull(sound.source()).name());
        node.node("volume").set(sound.volume());
        node.node("pitch").set(sound.pitch());
    }
}
