package io.github.wickeddroid.api.team;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ScatterTask {
    @NotNull CompletableFuture<List<Location>> scatterTask(String worldName, int maxX, int maxZ, int count) throws Exception;
}
