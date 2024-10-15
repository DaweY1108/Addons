package me.dawey.addons.plugincommands;

import me.dawey.addons.Addons;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddonsCommand implements CommandExecutor {
    private Addons plugin;

    public AddonsCommand(Addons plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /addons <reload>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("addons.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /addons <reload>");
        return true;
    }
}
