package io.github.wickeddroid.plugin.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Experimental
public class ScatterTask {


    public static @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count) throws Exception {
        Constructor<?> task;
        try {
            String className = "io.github.wickeddroid.adapter."+VERSION+".SafeScatter";

            task = Class.forName(className).getConstructor();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new ExceptionInInitializerError("Unsupported Experimental Scatter version.");
        }

        io.github.wickeddroid.api.team.ScatterTask scatterTask = (io.github.wickeddroid.api.team.ScatterTask) task.newInstance();

        return scatterTask.scatterTask(worldName, maxX, maxZ, count);
    }


    public static final String CRAFTBUKKIT = "org.bukkit.craftbukkit";
    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(CRAFTBUKKIT.length() + 1);
}
