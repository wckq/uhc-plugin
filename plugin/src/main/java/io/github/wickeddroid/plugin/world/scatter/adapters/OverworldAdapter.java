package io.github.wickeddroid.plugin.world.scatter.adapters;

import io.github.wickeddroid.api.world.EnvironmentAdapter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import team.unnamed.inject.Singleton;

import java.util.List;

@Singleton
public class OverworldAdapter implements EnvironmentAdapter {
    @Override
    public World.Environment environment() {
        return World.Environment.NORMAL;
    }

    @Override
    public int highestY(World world, int x, int z) {
        EnvironmentAdapter.super.highestY(world, x, z);
        return world.getHighestBlockYAt(x, z);
    }

    @Override
    public boolean safe(Location location, List<Biome> bannedBiomes, boolean preventLiquid, boolean aboveSeaLevel) {
        if (location.getWorld().getBlockAt(location).isLiquid() && preventLiquid) {
            return false;
        }

        if(bannedBiomes.contains(location.getWorld().getBlockAt(location).getBiome())) {
            return false;
        }

        return !aboveSeaLevel || !(location.getY() <= 63);
    }
}
