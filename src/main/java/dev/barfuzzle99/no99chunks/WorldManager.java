package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldManager {

    private static final Random rng = new Random();
    private static final List<World> no99ChunksWorlds = new ArrayList<>();

    public static List<World> getNo99ChunksWorlds() {
        return no99ChunksWorlds;
    }

    void updateNo99ChunksWorldList() {
        for (World world : Bukkit.getWorlds()) {
            if (WorldManager.isNo99ChunksWorld(world)) {
                no99ChunksWorlds.add(world);
            }
        }
    }

    public void createNo99ChunksWorld() {
        createNo99ChunksWorld(rng.nextInt());
    }

    public void createNo99ChunksWorld(String seed) {
        createNo99ChunksWorld(seed.hashCode());
    }

    public void createNo99ChunksWorld(int seed) {
        LazySpawnChunksEraser lazySpawnChunksEraser = new LazySpawnChunksEraser();

        new BukkitRunnable() {
            @Override
            public void run() {
                World overworld = WorldCreator
                        .name("no99chunks")
                        .type(WorldType.NORMAL)
                        .environment(World.Environment.NORMAL)
                        .seed(seed)
                        .createWorld();
                overworld.setKeepSpawnInMemory(false);
                lazySpawnChunksEraser.addToQueue(overworld);
            }
        }.runTask(No99Chunks.getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                World nether = WorldCreator
                        .name("no99chunks_nether")
                        .type(WorldType.NORMAL)
                        .environment(World.Environment.NETHER)
                        .seed(seed)
                        .createWorld();
                nether.setKeepSpawnInMemory(false);
                lazySpawnChunksEraser.addToQueue(nether);
            }
        }.runTaskLater(No99Chunks.getInstance(), 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                World end = WorldCreator
                        .name("no99chunks_the_end")
                        .type(WorldType.NORMAL)
                        .environment(World.Environment.THE_END)
                        .seed(seed)
                        .createWorld();
                end.setKeepSpawnInMemory(false);
                lazySpawnChunksEraser.addToQueue(end);
            }
        }.runTaskLater(No99Chunks.getInstance(), 2);

        lazySpawnChunksEraser.runTaskTimer(No99Chunks.getInstance(), 3, 1);
    }

    public  static boolean isNo99ChunksWorld(World world) {
        return world.getName().contains("no99chunks");
    }
}