package io.github.wickeddroid.adapter.v1_19_R2;

import io.github.wickeddroid.api.team.ScatterTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class SafeScatter implements ScatterTask {
    @Override
    public @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count, Consumer<Integer> progress) throws Exception {
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
        var chunkStatusFull = chunkStatus.getField("o").get(null);
        var prioritisedExecutor = Arrays.stream(prioritisedExecutorPriority.getEnumConstants()).toList().get(2);

        for(int i = 0; locations.size() < count; i++) {
            final var x = ThreadLocalRandom.current().nextInt(-maxX, maxX);
            final var z = ThreadLocalRandom.current().nextInt(-maxZ, maxX);

            final var y = world.getHighestBlockYAt(x, z);

            var chunkX = x >> 4;
            var chunkZ = z >> 4;

            Consumer<?> consumer = (Consumer<Object>) o -> {
                var loc = new Location(world, x, y, z);

                if (loc.getWorld().getBlockAt(loc).isLiquid()) {
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
}
