package dev.barfuzzle99.no99chunks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CmdNo99Chunks implements TabExecutor {

    private static final String prefix = No99Chunks.getPrefix();
    private static final String invalidUsageMsg = ChatColor.RED + "Invalid usage. Use /no99chunks help for help";
    private static final String noPermsMsg = ChatColor.RED + "You don't have permission to perform this command.";

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
            case "delete":
                return cmdDelete(sender, command, label, args);
            case "help":
                return cmdHelp(sender, command, label, args);
            default:
                sender.sendMessage(prefix + " " + invalidUsageMsg);
                break;
        }
        return false;
    }

    public boolean cmdJoin(CommandSender sender, Command command, String label, String[] args) {
        if (No99Chunks.getWorldManager().isBusy()) {
            sender.sendMessage(prefix + " the world manager is currently busy doing something, try again later");
            return false;
        }
        if (args.length > 1) {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " This is a player only command!");
            return false;
        }
        Player player = (Player) sender;
        if (WorldManager.getNo99ChunksWorlds().size() == 0) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " No worlds created yet!");
            return false;
        }
        if (WorldManager.isNo99ChunksWorld(player.getWorld())){
            sender.sendMessage(prefix + ChatColor.YELLOW + " You're already in a world without 99% of the chunks!");
            return false;
        }
        for (World no99chunksworld : WorldManager.getNo99ChunksWorlds()) {
            if (no99chunksworld.getEnvironment() == World.Environment.NORMAL) {
                ConfigUtil.savePlayerLastNormalWorldLoc(player, player.getLocation());
                Location lastNo99WorldLoc = ConfigUtil.getPlayerLastNo99WorldLoc(player);
                if (lastNo99WorldLoc != null && lastNo99WorldLoc.getWorld() != null) {
                    player.teleport(lastNo99WorldLoc);
                } else {
                    player.teleport(no99chunksworld.getSpawnLocation());
                }
            }
        }
        return false;
    }

    public boolean cmdCreate(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() && !sender.hasPermission("no99chunks.create")) {
            sender.sendMessage(noPermsMsg);
            return false;
        }
        if (!No99Chunks.isCompatibleWithCurrentNMSVersion()) {
            sender.sendMessage(prefix + ChatColor.RED + " It looks like the plugin is NOT compatible with your current server version. " +
                    "Please use 1.16.3 - 1.16.5 to ensure compatibility. This command will probably not work.");
        }

        if (No99Chunks.getWorldManager().isBusy()) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " the world manager is currently busy doing something, try again later");
            return false;
        }
        if (WorldManager.getNo99ChunksWorlds().size() > 0) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " You've already created worlds without 99% of the chunks!");
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
                    No99Chunks.getWorldManager().createNo99ChunksWorld(sender);
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
                    No99Chunks.getWorldManager().createNo99ChunksWorld(sender, seed);
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
        if (sender.isPermissionSet("no99chunks.leave") && !sender.hasPermission("no99chunks.leave")) {
            sender.sendMessage(noPermsMsg);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " This is a player only command!");
            return false;
        }
        Player player = (Player) sender;
        if (!WorldManager.isNo99ChunksWorld(player.getWorld())) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " You can only leave from a world without 99% of the chunks");
            return false;
        }

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

    public boolean cmdHelp(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isPermissionSet("no99chunks.help") && !sender.hasPermission("no99chunks.help")) {
            sender.sendMessage(noPermsMsg);
            return false;
        }
        sender.sendMessage(prefix + " Commands: \n" +
                "-/no99chunks help: shows this message\n"+
                "-/no99chunks join: joins the world without 99% of the chunks\n" +
                "-/no99chunks leave: leaves that world\n" +
                "-/no99chunks create [seed]: creates a world without 99% of the chunks\n" +
                "-/no99chunks delete: deletes the world without 99% of the chunks\n");
        return  false;
    }

    public boolean cmdDelete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() && !sender.hasPermission("no99chunks.delete")) {
            sender.sendMessage(noPermsMsg);
            return false;
        }
        if (No99Chunks.getWorldManager().isBusy()) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " the world manager is currently busy doing something, try again later");
            return false;
        }
        if (args.length == 1) {
            sender.sendMessage(prefix + ChatColor.YELLOW + " This is not reversible! Do /no99chunks delete confirm if you're sure");
        } else if (args.length == 2) {
            if (args[1].equals("confirm")) {
                No99Chunks.getWorldManager().setIsBusy(true);
                //Teleport players out of the worlds before deleting
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (WorldManager.isNo99ChunksWorld(player.getWorld())) {
                        Location lastNormalWorldLoc = ConfigUtil.getPlayerLastNormalWorldLoc(player);
                        if (lastNormalWorldLoc != null) {
                            player.teleport(lastNormalWorldLoc);
                        } else {
                            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                        }
                    }
                }
                for (World world : Bukkit.getWorlds()) {
                    if (WorldManager.isNo99ChunksWorld(world)) {
                        try {
                            deleteFolder(world.getWorldFolder());
                            Bukkit.unloadWorld(world, false);
                        } catch ( Exception ex) {
                            No99Chunks.getInstance().getLogger().log(Level.WARNING, "Could not delete folder: " + world.getWorldFolder().getAbsolutePath().toString());
                            ex.printStackTrace();
                        }
                    }
                }
                No99Chunks.getPlayerLastLocationsYml().getFile().delete();
                No99Chunks.getPlayerLastLocationsYml().createFile();
                No99Chunks.getPlayerLastLocationsYml().loadYamlFromFile();
                No99Chunks.getWorldManager().setIsBusy(false);
                sender.sendMessage(prefix + " Done");
            }
        } else {
            sender.sendMessage(prefix + " " + invalidUsageMsg);
        }
        return false;
    }

    static void deleteFolder(File dir) {
        for (File subFile : dir.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        dir.delete();
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
                suggestions.add("delete");
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