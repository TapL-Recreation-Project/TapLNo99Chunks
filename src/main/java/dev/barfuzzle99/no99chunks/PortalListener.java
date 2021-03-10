package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.logging.Level;

public class PortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPortalUse(PlayerPortalEvent event) {
        Bukkit.getLogger().log(Level.INFO, "I caught that portal event");
        if (!event.getPlayer().getWorld().getName().contains("no99chunks")) {
            Bukkit.getLogger().log(Level.INFO, "Returning because player wasn't in appropriate world");
            return;
        }
        Bukkit.getLogger().log(Level.INFO, "Proceeding. The teleport cause was: " + event.getCause().toString());
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            World end = Bukkit.getWorld("no99chunks_the_end");
            if (end == null) {
                No99Chunks.instance.getLogger().log(Level.SEVERE, "could not find no99chunks_the_end, did you rename or delete the world?");
                return;
            }
            event.setTo(Bukkit.getWorld("no99chunks_the_end").getSpawnLocation());
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            World nether = Bukkit.getWorld("no99chunks_nether");
            if (nether == null) {
                No99Chunks.instance.getLogger().log(Level.SEVERE, "could not find no99chunks_nether, did you rename or delete the world?");
                return;
            }
            Location netherLoc = event.getPlayer().getLocation().clone();
            netherLoc.setX(netherLoc.getX() / 8);
            netherLoc.setZ(netherLoc.getZ() / 8);
            netherLoc.setWorld(nether);
            event.setTo(netherLoc);
        }

    }
}
