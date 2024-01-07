package io.github.wickeddroid.plugin.world;


import ca.spottedleaf.concurrentutil.executor.standard.PrioritisedExecutor;
import io.github.wickeddroid.plugin.util.PluginUtil;
import io.papermc.paper.chunk.system.ChunkSystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@ApiStatus.Experimental
public class SafeScatter {


    public static @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count) throws Exception {
        final var world = Bukkit.getWorld(worldName);
        List<Location> locations = new ArrayList<>();

        var craftWorldClass = getCraftBukkit("CraftWorld");

        var craftWorld = craftWorldClass.cast(world);
        var handleField = craftWorld.getClass().getDeclaredField("world");
        handleField.setAccessible(true);
        var serverLevel = handleField.get(craftWorld);

        CompletableFuture<List<Location>> completableFuture = new CompletableFuture<>();


        for(int i = 0; locations.size() < count; i++) {
            final var x = ThreadLocalRandom.current().nextInt(-maxX, maxX);
            final var z = ThreadLocalRandom.current().nextInt(-maxZ, maxX);

            final var y = world.getHighestBlockYAt(x, z);

            var chunkX = x >> 4;
            var chunkZ = z >> 4;

            ChunkSystem.scheduleChunkLoad((ServerLevel) serverLevel, chunkX, chunkZ, false, ChunkStatus.FULL, true, PrioritisedExecutor.Priority.HIGHEST, chunkAccess -> {
                var loc = new Location(world, x, y, z);

                if(loc.getWorld().getBlockAt(loc).isLiquid()) { return; }

                locations.add(loc);
            });


            if(locations.size() >= count) {
                completableFuture.complete(locations);
            }
        }

        return completableFuture;
    }


    public static final String CRAFTBUKKIT = "org.bukkit.craftbukkit";
    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(CRAFTBUKKIT.length() + 1);
    protected static Class<?> getCraftBukkit(String name) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT + "." + VERSION + "." + name);
    }
}
