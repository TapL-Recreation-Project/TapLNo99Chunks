package dev.barfuzzle99.no99chunks;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Blocks;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;

public class LazySpawnChunksEraser extends BukkitRunnable {
    LinkedList<World> worldQueue = new LinkedList<World>();
    LinkedList<ChunkPos> chunkPosQueue = new LinkedList<>();
    CommandSender progressOutSender;
    private boolean finished = false;

    public LazySpawnChunksEraser(CommandSender progressOutSender) {
        this.progressOutSender = progressOutSender;
    }

    @Override
    public void run() {
        if (!this.isFinished()) {
            processQueue();
        } else {
            No99Chunks.getWorldManager().setIsBusy(false);
            progressOutSender.sendMessage("Finished world creation! Don't forget to import them to your Multi-world management plugin, if you have one, and configure per-world inventories in your inventory management plugin if you have one");
            this.cancel();
        }
    }

    public void enqueue(World world) {
        worldQueue.add(world);
    }

    public void processQueue() {
        if (worldQueue.isEmpty()) {
            this.finished = true;
            return;
        }
        if (chunkPosQueue.isEmpty()) {
            buildChunkQueue();
        } else {
            setChunkToAir(worldQueue.peek(), chunkPosQueue.peek());
            chunkPosQueue.poll();
            if (chunkPosQueue.isEmpty()) {
                worldQueue.poll();
                progressOutSender.sendMessage((3 - worldQueue.size()) + "/3 worlds created...");
            }
        }
    }

    public void buildChunkQueue() {
        final int SPAWN_CHUNKS_RADIUS = 4;
        final Location worldSpawnLoc = worldQueue.peek().getSpawnLocation();
        ChunkPos spawnChunkPos = new ChunkPos(worldSpawnLoc.getBlockX() >> 4, worldSpawnLoc.getBlockZ() >> 4);

        for (int dx = -SPAWN_CHUNKS_RADIUS; dx <= SPAWN_CHUNKS_RADIUS; dx++) {
            for (int dz = -SPAWN_CHUNKS_RADIUS; dz <= SPAWN_CHUNKS_RADIUS; dz++) {
                if (dx == 0 && dz == 0) continue;
                chunkPosQueue.add(new ChunkPos(spawnChunkPos.x + dx, spawnChunkPos.z + dz));
            }
        }
    }

    public void setChunkToAir(World world, ChunkPos chunkLoc) {
        int blockX = chunkLoc.x * 16;
        int blockZ = chunkLoc.z * 16;
        final int CHUNK_WIDTH = 16;

        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        for (int x = blockX; x < blockX + CHUNK_WIDTH; x ++) {
            for (int z = blockZ; z < blockZ + CHUNK_WIDTH; z ++) {
                int yTop = world.getHighestBlockYAt(x, z, HeightMap.WORLD_SURFACE);
                for (int y = 0; y <= yTop; y++) {
                    nmsSetToAir(nmsWorld, new BlockPosition(x, y, z));
                }
            }
        }
    }

    public static void nmsSetToAir(net.minecraft.server.v1_16_R3.World nmsWorld, BlockPosition blockPos) {
        final boolean applyPhysics = false;
        nmsWorld.setTypeAndData(blockPos, Blocks.AIR.getBlockData(), applyPhysics ? 3 : 2);
    }

    public static class ChunkPos {
        public int x;
        public int z;

        public ChunkPos(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    public boolean isFinished() {
        return this.finished;
    }
}
