package dev.barfuzzle99.no99chunks;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
            case "teleport":
                return cmdTeleport(sender, command, label, args);
            case "create":
                return cmdCreate(sender, command, label, args);
            default:
                sender.sendMessage(prefix + " " + invalidUsageMsg);
                break;
        }
        return false;
    }

    public boolean cmdTeleport(CommandSender sender, Command command, String label, String[] args) {
        WorldManager.updateNo99ChunksWorldList();
        if (args.length > 1) {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " This is a player only command");
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
                    player.teleport(no99chunksworld.getSpawnLocation());
                }
            }
        }
        return false;
    }

    public boolean cmdCreate(CommandSender sender, Command command, String label, String[] args) {
        WorldManager.updateNo99ChunksWorldList();
        if (WorldManager.getNo99ChunksWorlds().size() > 0) {
            sender.sendMessage(prefix + ChatColor.YELLOW + "You've already created worlds without 99% of the chunks!");
            return false;
        }
        if (args.length == 1) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " Creating the worlds can take around a minute, and, during that time, your server will lag behind.");
            sender.sendMessage(ChatColor.YELLOW + "But, after it's done, there'll be no more lag. Do /no99chunks create confirm whenever you're ready.");
            return false;
        } else if (args.length == 2) {
            if (args[1].equals("confirm")) {
                sender.sendMessage(prefix + " Creating worlds. This will take a while.");
                WorldManager.createNo99ChunksWorld();
            } else {
                sender.sendMessage(prefix + " " + invalidUsageMsg);
            }
        } else {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("create");
            suggestions.add("teleport");
            suggestions.add("help");
        }
        return suggestions;
    }
}
