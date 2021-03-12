package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ConfigUtil {
    public static List<String> stringListFromLocation(Location loc) {
        if (loc == null) {
            return null;
        }
        List<String> ret = new ArrayList<>();
        ret.add(Double.toString(loc.getX()));
        ret.add(Double.toString(loc.getY()));
        ret.add(Double.toString(loc.getZ()));
        ret.add(loc.getWorld().getName());
        return ret;
    }

    @Nullable
    public static Location locationFromStringList(List<String> strList) {
        if (strList == null) {
            return null;
        }
        try {
            double[] coords = new double[3];
            String worldName;
            coords[0] = Double.parseDouble(strList.get(0));
            coords[1] = Double.parseDouble(strList.get(0));
            coords[2] = Double.parseDouble(strList.get(0));
            worldName = strList.get(0);
            World world = Bukkit.getWorld(worldName);
            return new Location(Bukkit.getWorld(worldName), coords[0], coords[1], coords[2]);
        } catch (NumberFormatException ex) {
            No99Chunks.getInstance().getLogger().log(Level.WARNING, "Invalid numeric data in location: " + strList.toString());
        } catch (IndexOutOfBoundsException ex) {
            No99Chunks.getInstance().getLogger().log(Level.WARNING, "Insufficient data in location: " + strList.toString());
        }
        return null;
    }

    public static void savePlayerLastNo99WorldLoc(Player player, Location loc) {
        No99Chunks.getPlayerLastLocationsYml().yamlConfig().set(player.getName() + "lastNo99WorldPos", stringListFromLocation(loc));
        No99Chunks.getPlayerLastLocationsYml().saveChanges();
    }

    public static void savePlayerLastNormalWorldLoc(Player player, Location loc) {
        No99Chunks.getPlayerLastLocationsYml().yamlConfig().set(player.getName() + "lastNormalWorldPos", stringListFromLocation(loc));
        No99Chunks.getPlayerLastLocationsYml().saveChanges();
    }

    @Nullable
    public static Location getPlayerLastNo99WorldLoc(Player player) {
        List<String> ret = No99Chunks.getPlayerLastLocationsYml().yamlConfig().getStringList(player.getName() + ".lastNo99WorldPos");
        return locationFromStringList(ret);
    }

    @Nullable
    public static Location getPlayerLastNormalWorldLoc(Player player) {
        List<String> ret = No99Chunks.getPlayerLastLocationsYml().yamlConfig().getStringList(player.getName() + ".lastNormalWorldPos");
        return locationFromStringList(ret);
    }
}