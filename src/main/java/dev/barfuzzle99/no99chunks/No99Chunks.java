package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
}
