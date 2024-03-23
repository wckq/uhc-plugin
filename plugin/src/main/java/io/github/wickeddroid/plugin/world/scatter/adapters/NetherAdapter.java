package io.github.wickeddroid.plugin.world.scatter.adapters;

import io.github.wickeddroid.api.world.EnvironmentAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import team.unnamed.inject.Singleton;

import java.util.List;

@Singleton
public class NetherAdapter implements EnvironmentAdapter {

    @Override
    public World.Environment environment() {
        return World.Environment.NETHER;
    }

    @Override
    public int highestY(World world, int x, int z) {
        EnvironmentAdapter.super.highestY(world, x, z);
        int currentY = -1;

        for(int y = 125 ; y > 0 ; y--) {
            var block = world.getBlockAt(x, y, z);

            if(block.getType() != Material.AIR) { continue; }

            currentY = y+1;
        }

        return currentY;
    }

    @Override
    public boolean safe(Location location, List<Biome> bannedBiomes, boolean preventLiquid, boolean aboveSeaLevel) {
        var belowBlock = location.getWorld().getBlockAt((int) location.getX(), (int) (location.getY()-1), (int) location.getZ());

        var upBlock = location.getWorld().getBlockAt((int) location.getX(), (int) (location.getY()+1), (int) location.getZ());

        if(!belowBlock.getType().isSolid() || belowBlock.getType() == Material.LAVA || belowBlock.getType() == Material.MAGMA_BLOCK) {
            return false;
        }

        if(upBlock.getType() != Material.AIR) {
            return false;
        }

        if (location.getWorld().getBlockAt(location).isLiquid() && preventLiquid) {
            return false;
        }

        if(bannedBiomes.contains(location.getWorld().getBlockAt(location).getBiome())) {
            return false;
        }

        if(location.getY() <= 32 && aboveSeaLevel) {
            return false;
        }

        return !(location.getY() >= 127);
    }
}
