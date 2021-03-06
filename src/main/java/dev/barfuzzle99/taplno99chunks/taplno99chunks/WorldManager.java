package dev.barfuzzle99.taplno99chunks.taplno99chunks;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

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
        // TODO: check if these worlds already exist
        // TODO: track progress here

        World overworld = WorldCreator
                .name("no99chunks")
                .type(WorldType.NORMAL)
                .environment(World.Environment.NORMAL)
                .createWorld();
        overworld.setKeepSpawnInMemory(false);
        eraseSpawnChunksAround(overworld, 3);

        World nether = WorldCreator
                .name("no99chunks_nether")
                .type(WorldType.NORMAL)
                .environment(World.Environment.NETHER)
                .createWorld();
        nether.setKeepSpawnInMemory(false);
        eraseSpawnChunksAround(nether, 3);

        World end = WorldCreator
                .name("no99chunks_the_end")
                .type(WorldType.NORMAL)
                .environment(World.Environment.THE_END)
                .createWorld();
        end.setKeepSpawnInMemory(false);
        eraseSpawnChunksAround(end, 3);

        no99ChunksWorlds.add(overworld);
        no99ChunksWorlds.add(nether);
        no99ChunksWorlds.add(end);
    }

    // This is necessary because the spawn chunks aren't affected by our custom ChunkGenerator
    public static void eraseSpawnChunksAround(World world, int chunkRadius) {
        final int CHUNK_SIDE = 16;
        final int spawnProtectionSide = (2 * chunkRadius + 1) * CHUNK_SIDE;
        final int xOrigin = world.getSpawnLocation().getBlockX();
        final int zOrigin = world.getSpawnLocation().getBlockZ();
        final int sweepMin = -spawnProtectionSide / 2;
        final int sweepMax = -sweepMin;

        for (int dx = sweepMin; dx < sweepMax; dx ++) {
            for (int dz = sweepMin; dz < sweepMax; dz ++) {
                // Don't erase the spawn chunk
                if (Math.abs(dx) < CHUNK_SIDE / 2 && Math.abs(dz) < CHUNK_SIDE / 2) {
                    continue;
                }
                int yMax = world.getHighestBlockYAt(xOrigin + dx, zOrigin + dz);
                for (int y = 0; y <= yMax; y++) {
                    nmsSetToAir(world, xOrigin + dx, y, zOrigin + dz);
                }
            }
        }
    }

    // TODO: check if there's potential for optimization
    public static void nmsSetToAir(World world, int x, int y, int z) {
        final boolean applyPhysics = false;
        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        nmsWorld.setTypeAndData(new BlockPosition(x, y, z), Blocks.AIR.getBlockData(), applyPhysics ? 3 : 2);
    }
}
