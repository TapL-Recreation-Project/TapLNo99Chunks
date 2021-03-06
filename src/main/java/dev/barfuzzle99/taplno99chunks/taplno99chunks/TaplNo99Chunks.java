package dev.barfuzzle99.taplno99chunks.taplno99chunks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TaplNo99Chunks extends JavaPlugin {
    @Override
    public void onEnable() {
        registerListeners();
        registerCommands();
        WorldManager.updateNo99ChunksWorldList();
    }

    public void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    public void registerCommands() {
        this.getCommand("taplno99chunks").setExecutor(new CmdNo99Chunks());
        Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }
}
