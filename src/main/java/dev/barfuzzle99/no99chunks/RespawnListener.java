package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.broadcastMessage("Caught respawn even for player " + event.getPlayer().getName() + " in world " + event.getPlayer().getWorld());
        Bukkit.broadcastMessage("Is it no 99% chunks world? " + "" + WorldManager.isNo99ChunksWorld(event.getPlayer().getWorld()));
        if (!WorldManager.isNo99ChunksWorld(event.getPlayer().getWorld())) {
            return;
        }
        // Fix for Multiverse (and possibly other world management plugins) trying to respawn player in the void
        // Takes precedence over bed/anchor spawns because the spawn location will be considered a bed spawn in such cases
        if ((event.getRespawnLocation().getWorld().getHighestBlockYAt(event.getRespawnLocation().getBlockX(),
                event.getRespawnLocation().getBlockZ()) == 0)) {
            event.setRespawnLocation(WorldManager.getNo99ChunksOverworld().getSpawnLocation());
        } else if (event.isBedSpawn() || event.isAnchorSpawn()) {
            if (!WorldManager.isNo99ChunksWorld(event.getRespawnLocation().getWorld())) {
                Bukkit.broadcastMessage("Multiverse tried to spawn player in another world, cancelling this");
            } else {
                event.setRespawnLocation(WorldManager.getNo99ChunksOverworld().getSpawnLocation());
            }
        } else {
            Bukkit.broadcastMessage("Not a bed spawn, redirecting to spawn: " + WorldManager.getNo99ChunksOverworld().getSpawnLocation().toString());
            event.setRespawnLocation(WorldManager.getNo99ChunksOverworld().getSpawnLocation());
        }
    }
}
