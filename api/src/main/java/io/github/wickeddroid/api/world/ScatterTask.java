package io.github.wickeddroid.api.world;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface ScatterTask {
    @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count, boolean preventLiquid, boolean aboveSeaLevel, List<Biome> bannedBiomes, EnvironmentAdapter adapter, Consumer<Integer> progress) throws Exception;
}
