package dev.barfuzzle99.no99chunks;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.logging.Level;

public class LazySpawnChunksEraser extends BukkitRunnable {
    LinkedList<World> worldQueue = new LinkedList<World>();
    LinkedList<ChunkPos> chunkPosQueue = new LinkedList<>();
    CommandSender progressOutSender = Bukkit.getConsoleSender();
    private boolean finished = false;

    // TODO: allow sending progress to sender
    public void addToQueue(World world) {
        worldQueue.add(world);
    }

    @Override
    public void run() {
        if (!this.isFinished()) {
            processQueue();
        } else {
            progressOutSender.sendMessage("Finished world creation! Don't forget to import them to your Multi-world management plugin, if you have one.");
            this.cancel();
        }
    }

    public void processQueue() {
        if (worldQueue.isEmpty()) {
            this.finished = true;
            Bukkit.getLogger().log(Level.INFO, String.format("World queue is empty, exiting"));
            return;
        }
        if (chunkPosQueue.isEmpty()) {
            Bukkit.getLogger().log(Level.INFO, String.format("Chunk queue is empty, making a new one"));
            buildChunkQueue();
        } else {
            setChunkToAir(worldQueue.peek(), chunkPosQueue.peek());
            ChunkPos lastCleanedChunk = chunkPosQueue.peek();
            Bukkit.getLogger().log(Level.INFO, String.format("Cleaned chunk %d, %d of '%s'", lastCleanedChunk.x, lastCleanedChunk.z, worldQueue.peek().getName()));
            chunkPosQueue.poll();
            if (chunkPosQueue.isEmpty()) {
                Bukkit.getLogger().log(Level.INFO, String.format("Chunk queue ran out, going to remove world %s from queue", worldQueue.peek().getName()));
                worldQueue.poll();
                if (!worldQueue.isEmpty()) {
                    Bukkit.getLogger().log(Level.INFO, String.format("World %s is now the head of the queue", worldQueue.peek().getName()));
                }
            }
        }
    }

    public void buildChunkQueue() {
        final int SPAWN_CHUNKS_RADIUS = 2;
        final Location worldSpawnLoc = worldQueue.peek().getSpawnLocation();
        ChunkPos spawnChunkPos = new ChunkPos(worldSpawnLoc.getBlockX() >> 4, worldSpawnLoc.getBlockZ() >> 4);

        for (int dx = -SPAWN_CHUNKS_RADIUS; dx <= SPAWN_CHUNKS_RADIUS; dx++) {
            for (int dz = -SPAWN_CHUNKS_RADIUS; dz <= SPAWN_CHUNKS_RADIUS; dz++) {
                if (dx == 0 && dz == 0) continue;
                chunkPosQueue.add(new ChunkPos(spawnChunkPos.x + dx, spawnChunkPos.z + dz));
                Bukkit.getLogger().log(Level.INFO, String.format("Added chunk %d %d to queue", spawnChunkPos.x + dx, spawnChunkPos.z + dz));
            }
        }
    }

    int cleanedBlocks = 0;
    public void setChunkToAir(World world, ChunkPos chunkLoc) {
        int blockX = chunkLoc.x * 16;
        int blockZ = chunkLoc.z * 16;
        final int CHUNK_WIDTH = 16;

        net.minecraft.server.v1_16_R3.World nmsWorld = ((CraftWorld) world).getHandle();
        for (int x = blockX; x < blockX + CHUNK_WIDTH; x ++) {
            for (int z = blockZ; z < blockZ + CHUNK_WIDTH; z ++) {
                int yTop = world.getHighestBlockYAt(x, z);
                for (int y = 0; y <= yTop; y++) {
                    nmsSetToAir(nmsWorld, new BlockPosition(x, y, z));
                    cleanedBlocks++;
                    if (cleanedBlocks % 50000 == 0) {
                        Bukkit.getLogger().log(Level.INFO, String.format("cleaned %d blocks", cleanedBlocks));
                    }
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
