package dev.barfuzzle99.no99chunks;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!WorldManager.isNo99ChunksWorld(event.getPlayer().getWorld())) {
            return;
        }
        // Fix for Multiverse (and possibly other world management plugins) trying to respawn player in the void
        // Takes precedence over bed/anchor spawns because the spawn location will be considered a bed spawn in such cases\
        Location overworldSpawn = WorldManager.getNo99ChunksOverworld().getSpawnLocation();
        if ((event.getRespawnLocation().getWorld().getHighestBlockYAt(event.getRespawnLocation().getBlockX(),
                event.getRespawnLocation().getBlockZ()) == 0)) {
            event.setRespawnLocation(overworldSpawn);
        } else if (event.isBedSpawn() || event.isAnchorSpawn()) {
            if (!WorldManager.isNo99ChunksWorld(event.getRespawnLocation().getWorld())) {
                event.setRespawnLocation(overworldSpawn);
            }
        } else {
            event.setRespawnLocation(overworldSpawn);
        }
    }
}
