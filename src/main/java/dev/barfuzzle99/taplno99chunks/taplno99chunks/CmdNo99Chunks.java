package dev.barfuzzle99.taplno99chunks.taplno99chunks;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdNo99Chunks implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO : proper command handling and formatting; add prefixes
        String invalidUsageMsg = "[TapLNo99Chunks] Invalid usage. Use /taplno99chunks create|teleport";

        if (args.length == 0) {
            sender.sendMessage(invalidUsageMsg);
            return false;
        }

        switch (args[0]) {
            case "teleport":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("This is a player only command");
                    return false;
                }
                if (WorldManager.getNo99ChunksWorlds().size() == 0) {
                    sender.sendMessage("No worlds created yet");
                    return false;
                } else {
                    for (World world : WorldManager.getNo99ChunksWorlds()) {
                        if (world.getEnvironment() == World.Environment.NORMAL) {
                            ((Player) sender).teleport(world.getSpawnLocation());
                        }
                    }
                }
                break;
            case "create":
                sender.sendMessage("Creating world. This will take a while.");
                WorldManager.createNo99ChunksWorld();
                sender.sendMessage("Done!");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // TODO
        return null;
    }
}
