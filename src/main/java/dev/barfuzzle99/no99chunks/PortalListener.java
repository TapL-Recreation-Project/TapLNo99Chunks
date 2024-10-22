package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.logging.Level;

public class PortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPortalUse(PlayerPortalEvent event) {

        if (!WorldManager.isNo99ChunksWorld(event.getPlayer().getWorld())) {
            return;
        }

        switch (event.getCause()) {
            case END_PORTAL:
                World end = Bukkit.getWorld("no99chunks_the_end");
                if (end == null) {
                    No99Chunks.getInstance().getLogger().log(Level.SEVERE, "could not find no99chunks_the_end, did you rename or delete the world?");
                    return;
                }
                event.setTo(Bukkit.getWorld("no99chunks_the_end").getSpawnLocation());
                break;
            case NETHER_PORTAL:
                World nether = Bukkit.getWorld("no99chunks_nether");
                World overworld = Bukkit.getWorld("no99chunks");

                if (nether == null) {
                    No99Chunks.getInstance().getLogger().log(Level.SEVERE, "could not find world no99chunks_nether, did you rename or delete the world?");
                    return;
                }
                if (overworld == null) {
                    No99Chunks.getInstance().getLogger().log(Level.SEVERE, "could not find world no99chunks, did you rename or delete the world?");
                    return;
                }

                // Player coming back from the nether
                if (event.getPlayer().getWorld().equals(nether)) {
                    Location overworldLoc = event.getPlayer().getLocation().clone();
                    overworldLoc.setX(overworldLoc.getX() * 8);
                    overworldLoc.setZ(overworldLoc.getZ() * 8);
                    overworldLoc.setWorld(overworld);
                    event.setTo(overworldLoc);
                    // Player is going to the nether
                } else {
                    Location netherLoc = event.getPlayer().getLocation().clone();
                    netherLoc.setX(netherLoc.getX() / 8);
                    netherLoc.setZ(netherLoc.getZ() / 8);
                    netherLoc.setWorld(nether);
                    event.setTo(netherLoc);
                }
                break;
            default:
                break;
        }
    }
}
