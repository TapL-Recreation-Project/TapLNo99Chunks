package dev.barfuzzle99.taplno99chunks.taplno99chunks;

import java.lang.reflect.Field;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import net.minecraft.server.v1_16_R3.ChunkGenerator;
import net.minecraft.server.v1_16_R3.PlayerChunkMap;

public class WorldListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldInit(WorldInitEvent event){
        inject(event.getWorld());
    }

    private void inject(World bukkitWorld) {
        final CraftWorld world = (CraftWorld) bukkitWorld;
        try {
            @SuppressWarnings("resource")
            final PlayerChunkMap playerChunkMap = world.getHandle().getChunkProvider().playerChunkMap;
            final Field ChunkGeneratorField = PlayerChunkMap.class.getDeclaredField("chunkGenerator");
            ChunkGeneratorField.setAccessible(true);
            final Object chunkGeneratorObject = ChunkGeneratorField.get(playerChunkMap);
            final ChunkGenerator chunkGenerator = (ChunkGenerator) chunkGeneratorObject;
            final ChunkOverrider overrider = new ChunkOverrider(chunkGenerator);
            ChunkGeneratorField.set(playerChunkMap, overrider);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error injecting to world " + world.getName());
        }
    }
}
