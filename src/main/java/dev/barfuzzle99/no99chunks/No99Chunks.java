package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class No99Chunks extends JavaPlugin {

    private static No99Chunks instance;
    private static Config playerLastLocationsYml;
    public static WorldManager worldManager;

    @Override
    public void onEnable() {
        instance = this;
        worldManager = new WorldManager();
        initConfig();
        registerListeners();
        registerCommands();
        new Metrics(this, 10671);
        if (!isCompatibleWithCurrentNMSVersion()) {
            this.getLogger().log(Level.SEVERE, "It looks like the plugin is NOT compatible with your current server version. " +
                    "Please use 1.16.3 - 1.16.5 to ensure compatibility.");
        }
    }

    public void initConfig() {
        playerLastLocationsYml = new Config(this, "last_locations.yml");
        if (!playerLastLocationsYml.getFile().exists()) {
            playerLastLocationsYml.createFile();
        }
    }

    public void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new WorldInitListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PortalListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RespawnListener(), this);
    }

    public void registerCommands() {
        this.getCommand("no99chunks").setExecutor(new CmdNo99Chunks());
        this.getCommand("no99chunks").setTabCompleter(new CmdNo99Chunks());
        Bukkit.getServer().getPluginManager().registerEvents(new WorldInitListener(), this);
    }

    public static WorldManager getWorldManager() {
        return worldManager;
    }

    public static Config getPlayerLastLocationsYml() {
        return playerLastLocationsYml;
    }

    public static No99Chunks getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return "[No99Chunks]";
    }

    public static boolean isCompatibleWithCurrentNMSVersion() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        ver = ver.substring(ver.lastIndexOf('.') + 1);
        return ver.equals("v1_16_R3");
    }
}
