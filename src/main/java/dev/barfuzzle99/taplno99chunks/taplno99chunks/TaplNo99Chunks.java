package dev.barfuzzle99.taplno99chunks.taplno99chunks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TaplNo99Chunks extends JavaPlugin {
    @Override
    public void onEnable() {
        registerListeners();
    }

    public void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }
}
