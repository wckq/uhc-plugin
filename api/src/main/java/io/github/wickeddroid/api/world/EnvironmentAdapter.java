package io.github.wickeddroid.api.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.List;


public interface EnvironmentAdapter {
    World.Environment environment();

    default int highestY(World world, int x, int z) {
        this.checkSupport(world);

        return 0;
    }

    boolean safe(Location location, List<Biome> bannedBiomes, boolean preventLiquid, boolean aboveSeaLevel);

    default void checkSupport(World world) {
        if(world.getEnvironment() == environment()) {
            throw new RuntimeException("Unsupported Environment for adapter");
        }
    }
}
