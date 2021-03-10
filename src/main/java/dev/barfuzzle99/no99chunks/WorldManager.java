package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {

    private static final List<World> no99ChunksWorlds = new ArrayList<>();

    public static List<World> getNo99ChunksWorlds() {
        return no99ChunksWorlds;
    }

    public static void updateNo99ChunksWorldList() {
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().contains("no99chunks")) {
                no99ChunksWorlds.add(world);
            }
        }
    }

    public static void createNo99ChunksWorld() {
        LazySpawnChunksEraser lazySpawnChunksEraser = new LazySpawnChunksEraser();

        new BukkitRunnable() {
            @Override
            public void run() {
                World overworld = WorldCreator
                        .name("no99chunks")
                        .type(WorldType.NORMAL)
                        .environment(World.Environment.NORMAL)
                        .createWorld();
                overworld.setKeepSpawnInMemory(false);
                lazySpawnChunksEraser.addToQueue(overworld);
            }
        }.runTask(No99Chunks.instance);

        new BukkitRunnable() {
            @Override
            public void run() {
                World nether = WorldCreator
                        .name("no99chunks_nether")
                        .type(WorldType.NORMAL)
                        .environment(World.Environment.NETHER)
                        .createWorld();
                nether.setKeepSpawnInMemory(false);
                lazySpawnChunksEraser.addToQueue(nether);
            }
        }.runTaskLater(No99Chunks.instance, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                World end = WorldCreator
                        .name("no99chunks_the_end")
                        .type(WorldType.NORMAL)
                        .environment(World.Environment.THE_END)
                        .createWorld();
                end.setKeepSpawnInMemory(false);
                lazySpawnChunksEraser.addToQueue(end);
            }
        }.runTaskLater(No99Chunks.instance, 2);

        lazySpawnChunksEraser.runTaskTimer(No99Chunks.instance, 3, 1);
    }
}
