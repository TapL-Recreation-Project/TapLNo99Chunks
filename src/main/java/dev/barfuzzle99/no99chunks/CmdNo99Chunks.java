package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CmdNo99Chunks implements TabExecutor {

    private static final String prefix = No99Chunks.getPrefix();
    private static final String invalidUsageMsg = ChatColor.RED + "Invalid usage. Use /no99chunks help for help";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO : proper command handling and formatting; add prefixes

        if (args.length == 0) {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
            return false;
        }

        switch (args[0]) {
            case "join":
                return cmdJoin(sender, command, label, args);
            case "create":
                return cmdCreate(sender, command, label, args);
            case "leave":
                return cmdLeave(sender, command, label, args);
            default:
                sender.sendMessage(prefix + " " + invalidUsageMsg);
                break;
        }
        return false;
    }

    public boolean cmdJoin(CommandSender sender, Command command, String label, String[] args) {
        No99Chunks.getWorldManager().updateNo99ChunksWorldList();
        if (args.length > 1) {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " This is a player only command!");
            return false;
        }
        if (WorldManager.getNo99ChunksWorlds().size() == 0) {
            sender.sendMessage(prefix + ChatColor.YELLOW + "No worlds created yet!");
            return false;
        }
        Player player = (Player) sender;
        for (World no99chunksworld : WorldManager.getNo99ChunksWorlds()) {
            if (no99chunksworld.getEnvironment() == World.Environment.NORMAL) {
                if (player.getWorld().getName().contains("no99chunks")) {
                    player.sendMessage(prefix + " you're already in a world without 99% of the chunks!");
                    return false;
                } else {
                    ConfigUtil.savePlayerLastNormalWorldLoc(player, player.getLocation());
                    Location lastNo99WorldLoc = ConfigUtil.getPlayerLastNo99WorldLoc(player);
                    if (lastNo99WorldLoc != null && lastNo99WorldLoc.getWorld() != null) {
                        player.teleport(lastNo99WorldLoc);
                    } else {
                        player.teleport(no99chunksworld.getSpawnLocation());
                    }
                }
            }
        }
        return false;
    }

    public boolean cmdCreate(CommandSender sender, Command command, String label, String[] args) {
        No99Chunks.getWorldManager().updateNo99ChunksWorldList();
        if (WorldManager.getNo99ChunksWorlds().size() > 0) {
            sender.sendMessage(prefix + ChatColor.YELLOW + "You've already created worlds without 99% of the chunks!");
            return false;
        }

        switch (args.length) {
            case 1: // no99chunks create
                sender.sendMessage(prefix + ChatColor.YELLOW + " Creating the worlds can take around a minute, and, during that time, your server will lag behind.");
                sender.sendMessage(ChatColor.YELLOW + "But, after it's done, there'll be no more lag. Do /no99chunks create confirm whenever you're ready.");
                return false;
            case 2: // no99chunks create confirm OR no99chunks create seed
                if (args[1].equals("confirm")) {
                    // no99chunks create confirm
                    sender.sendMessage(prefix + ChatColor.GREEN + " World creation started");
                    No99Chunks.getWorldManager().createNo99ChunksWorld();
                } else {
                    // no99chunks create seed
                    String seed = args[1];
                    sender.sendMessage(prefix + ChatColor.YELLOW + " Creating the worlds can take around a minute, and, during that time, your server will lag behind.");
                    sender.sendMessage(ChatColor.YELLOW + "But, after it's done, there'll be no more lag. Do /no99chunks create " + seed + " confirm whenever you're ready.");
                }
                return false;
            case 3: // no99chunks create seed confirm
                String seed = args[1];
                if (args[2].equals("confirm")) {
                    sender.sendMessage(prefix + ChatColor.GREEN + " World creation started");
                    No99Chunks.getWorldManager().createNo99ChunksWorld(seed);
                } else {
                    sender.sendMessage(prefix + " " + invalidUsageMsg);
                }
                return false;
            default:
                sender.sendMessage(prefix + " " + invalidUsageMsg);
                return false;
        }
    }

    public boolean cmdLeave(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " This is a player only command!");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 1) {
            ConfigUtil.savePlayerLastNo99WorldLoc(player, player.getLocation());
            Location loc = ConfigUtil.getPlayerLastNormalWorldLoc(player);
            if (loc == null) {
                No99Chunks.getInstance().getLogger().log(Level.SEVERE, "Could not get last location in normal world for " + player.getName() +
                        ". They'll be teleported to the main world. Is " + No99Chunks.getPlayerLastLocationsYml().getFile().getName() + " damaged?");
                player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                return false;
            }
            if (loc.getWorld() == null) {
                No99Chunks.getInstance().getLogger().log(Level.WARNING, "Player " + player.getName() +
                        " was in world that doesn't exist. Was it renamed or deleted?");
                player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                return false;
            }
            player.teleport(loc);
        } else {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        switch (args.length) {
            case 1:
                suggestions.add("create");
                suggestions.add("join");
                suggestions.add("help");
                suggestions.add("leave");
                break;
            case 2:
                if (args[0].equals("create")) {
                    suggestions.add("[seed]");
                }
                break;

        }
        return suggestions;
    }
}
