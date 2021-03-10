package dev.barfuzzle99.no99chunks;

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
        if (event.getWorld().getName().contains("no99chunks")) {
            inject(event.getWorld());
        }
    }

    private void inject(World world) {
        final CraftWorld craftWorld = (CraftWorld) world;
        try {
            @SuppressWarnings("resource")
            final PlayerChunkMap playerChunkMap = craftWorld.getHandle().getChunkProvider().playerChunkMap;
            final Field ChunkGeneratorField = PlayerChunkMap.class.getDeclaredField("chunkGenerator");
            ChunkGeneratorField.setAccessible(true);
            final Object chunkGeneratorObject = ChunkGeneratorField.get(playerChunkMap);
            final ChunkGenerator chunkGenerator = (ChunkGenerator) chunkGeneratorObject;
            final ChunkOverrider overrider = new ChunkOverrider(chunkGenerator);
            ChunkGeneratorField.set(playerChunkMap, overrider);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error injecting to world " + craftWorld.getName());
        }
    }
}
