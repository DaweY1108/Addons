package me.dawey.addons.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor {
    public static void runCommand(String command, Player p, boolean isConsole) {
        if (command.contains("{player}"))
            command = command.replace("{player}", p.getName());
        if (command.contains("{location}"))
            command = command.replace("{location}", locationString(p.getLocation()));
        if (command.charAt(0) == '/')
            command = command.substring(1);
        if (isConsole) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
        } else {
            p.performCommand(command);
        }
    }

    private static String locationString(Location loc) {
        String x = String.valueOf(loc.getBlockX());
        String y = String.valueOf(loc.getBlockY());
        String z = String.valueOf(loc.getBlockZ());
        return x + " " + y + " " + z;
    }
}
