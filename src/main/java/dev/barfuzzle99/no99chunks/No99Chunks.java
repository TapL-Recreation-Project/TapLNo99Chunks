package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class No99Chunks extends JavaPlugin {

    public static No99Chunks instance;

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
        registerCommands();
        WorldManager.updateNo99ChunksWorldList();
    }

    public void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PortalListener(), this);
    }

    public void registerCommands() {
        this.getCommand("no99chunks").setExecutor(new CmdNo99Chunks());
        this.getCommand("no99chunks").setTabCompleter(new CmdNo99Chunks());
        Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    public static String getPrefix() {
        return "[No99Chunks]";
    }
}
