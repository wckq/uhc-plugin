package io.github.wickeddroid.plugin.world.scatter;

import io.github.wickeddroid.api.world.EnvironmentAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ScatterTask {


    public static @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count, boolean preventLiquid, boolean aboveSeaLevel, List<Biome> bannedBiomes, EnvironmentAdapter adapter, Consumer<Integer> progress) throws Exception {
        Constructor<?> task;
        try {
            String className = "io.github.wickeddroid.adapter."+VERSION+".SafeScatter";

            task = Class.forName(className).getConstructor();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new ExceptionInInitializerError("Unsupported Experimental Scatter version.");
        }

        io.github.wickeddroid.api.world.ScatterTask scatterTask = (io.github.wickeddroid.api.world.ScatterTask) task.newInstance();

        return scatterTask.scatterTask(worldName, maxX, maxZ, count, preventLiquid, aboveSeaLevel, bannedBiomes, adapter, progress);
    }


    public static final String CRAFTBUKKIT = "org.bukkit.craftbukkit";
    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(CRAFTBUKKIT.length() + 1);
}
