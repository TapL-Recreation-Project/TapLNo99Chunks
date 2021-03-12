package dev.barfuzzle99.no99chunks;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!WorldManager.isNo99ChunksWorld(event.getPlayer().getWorld())) {
            return;
        }
        if (!WorldManager.isNo99ChunksWorld(event.getRespawnLocation().getWorld())) {
            for (World world : WorldManager.getNo99ChunksWorlds()) {
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    event.setRespawnLocation(world.getSpawnLocation());
                    return;
                }
            }
        }
    }
}
