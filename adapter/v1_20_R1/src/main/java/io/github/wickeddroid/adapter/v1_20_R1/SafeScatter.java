package io.github.wickeddroid.adapter.v1_20_R1;

import io.github.wickeddroid.api.team.ScatterTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class SafeScatter implements ScatterTask {
    @Override
    public @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count, boolean preventLiquid, boolean aboveSeaLevel, List<Biome> bannedBiomes, Consumer<Integer> progress) throws Exception {
        final var world = Bukkit.getWorld(worldName);
        List<Location> locations = new ArrayList<>();

        var craftWorldClass = getCraftBukkit("CraftWorld");

        var craftWorld = craftWorldClass.cast(world);

        var handleField = craftWorld.getClass().getDeclaredField("world");
        handleField.setAccessible(true);
        var serverLevel = handleField.get(craftWorld);

        CompletableFuture<List<Location>> completableFuture = new CompletableFuture<>();

        var serverLevelCast = Class.forName("net.minecraft.server.level.WorldServer").cast(serverLevel);
        var chunkStatus = Class.forName("net.minecraft.world.level.chunk.ChunkStatus");
        var prioritisedExecutorPriority = Class.forName("ca.spottedleaf.concurrentutil.executor.standard.PrioritisedExecutor$Priority");

        var chunkSystem = Class.forName("io.papermc.paper.chunk.system.ChunkSystem");
        var scheduleLoad = chunkSystem.getMethod("scheduleChunkLoad", serverLevel.getClass(), int.class, int.class, chunkStatus, boolean.class, prioritisedExecutorPriority, Consumer.class);
        var chunkStatusFull = chunkStatus.getField("n").get(null);
        var prioritisedExecutor = Arrays.stream(prioritisedExecutorPriority.getEnumConstants()).toList().get(2);

        for(int i = 0; locations.size() < count; i++) {
            final var x = ThreadLocalRandom.current().nextInt(-maxX, maxX);
            final var z = ThreadLocalRandom.current().nextInt(-maxZ, maxX);

            final @Nullable Integer y = world.getEnvironment() == World.Environment.NETHER ? highestYNether(world, x, z) : Integer.valueOf(world.getHighestBlockYAt(x, z) + 1);

            if(y == null) {
                continue;
            }

            var chunkX = x >> 4;
            var chunkZ = z >> 4;

            Consumer<?> consumer = (Consumer<Object>) o -> {
                var loc = new Location(world, x, y, z);

                if (loc.getWorld().getBlockAt(loc).isLiquid() && preventLiquid) {
                    return;
                }

                if(bannedBiomes.contains(loc.getWorld().getBlockAt(loc).getBiome())) {
                    return;
                }

                if(aboveSeaLevel && world.getEnvironment() == World.Environment.NORMAL && y <= 63) {
                    return;
                }

                if(world.getEnvironment() == World.Environment.THE_END && (y == 0 || !world.getBlockAt(x, y-2, z).isSolid())) {
                    return;
                }

                locations.add(loc);
                progress.accept(locations.size());
            };

            if(locations.size() >= count) {
                completableFuture.complete(locations);
            } else {
                scheduleLoad.invoke(null, serverLevelCast, chunkX, chunkZ, chunkStatusFull, true, prioritisedExecutor, consumer);
            }
        }

        return completableFuture;
    }

    public static final String CRAFTBUKKIT = "org.bukkit.craftbukkit";
    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(CRAFTBUKKIT.length() + 1);
    protected static Class<?> getCraftBukkit(String name) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT + "." + VERSION + "." + name);
    }

    protected @Nullable Integer highestYNether(World world, int x, int z) {
        Integer currentY = null;
        for(int y = 127 ; y > 0 ; y--) {
            var block = world.getBlockAt(x, y, z);

            if(block.getType() != Material.AIR) { continue; }

            var upBlock =  world.getBlockAt(x, y+1, z);

            if(upBlock.getType() != Material.AIR) { continue; }

            var belowBlock = world.getBlockAt(x, y-1, z);

            if(!belowBlock.getType().isSolid() || belowBlock.getType() == Material.LAVA || belowBlock.getType() == Material.MAGMA_BLOCK) { continue; }

            currentY = y+1;
        }

        return currentY;
    }
}
