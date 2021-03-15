package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldManager {

    private static final Random rng = new Random();
    private static final List<World> no99ChunksWorlds = new ArrayList<>();
    public boolean isBusy = false;

    public static List<World> getNo99ChunksWorlds() {
        updateNo99ChunksWorldList();
        return no99ChunksWorlds;
    }

    private static void updateNo99ChunksWorldList() {
        no99ChunksWorlds.clear();
        for (World world : Bukkit.getWorlds()) {
            if (WorldManager.isNo99ChunksWorld(world)) {
                no99ChunksWorlds.add(world);
            }
        }
    }

    private static long getVanillaSeedFromString(String seedStr) {
        if (seedStr.length() >= 20) {
            return seedStr.hashCode();
        }
        try {
            long seedLong = Long.parseLong(seedStr);
            if (seedStr.equals(Long.toString(seedLong))) {
                return seedLong;
            } else {
                return seedStr.hashCode();
            }
        } catch (NumberFormatException ex) {
            return seedStr.hashCode();
        }
    }

    public void createNo99ChunksWorld(CommandSender sender) {
        createNo99ChunksWorld(sender, rng.nextLong());
    }

    public void createNo99ChunksWorld(CommandSender sender, String seed) {
        createNo99ChunksWorld(sender, getVanillaSeedFromString(seed));
    }

    public void createNo99ChunksWorld(CommandSender sender, long seed) {
        this.setIsBusy(true);
        LazySpawnChunksEraser lazySpawnChunksEraser = new LazySpawnChunksEraser(sender);

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
                overworld.getSpawnLocation().getBlock().setType(Material.BEDROCK);
                lazySpawnChunksEraser.enqueue(overworld);
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
                nether.getSpawnLocation().getBlock().setType(Material.BEDROCK);
                lazySpawnChunksEraser.enqueue(nether);
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
                end.getSpawnLocation().getBlock().setType(Material.BEDROCK);
                lazySpawnChunksEraser.enqueue(end);
            }
        }.runTaskLater(No99Chunks.getInstance(), 2);

        lazySpawnChunksEraser.runTaskTimer(No99Chunks.getInstance(), 3, 1);
    }

    public void setIsBusy(boolean busy) {
        this.isBusy = busy;
    }

    public boolean isBusy() {
        return this.isBusy;
    }

    public  static boolean isNo99ChunksWorld(World world) {
        return world.getName().contains("no99chunks");
    }
}