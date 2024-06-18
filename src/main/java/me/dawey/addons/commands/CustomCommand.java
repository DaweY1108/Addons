package me.dawey.addons.commands;

import me.dawey.addons.Addons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomCommand extends Command {
    private JavaPlugin plugin = Addons.getInstance();

    private List<String> command;

    private String execute;

    private CommandMap commandMap = null;

    public CustomCommand(String command, String execute) {
        super(command);
        List<String> s = new ArrayList<>();
        s.add(command);
        this.command = s;
        this.execute = execute;
        addCommand();
    }

    private void addCommand() {
        if (this.commandMap == null)
            try {
                Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                this.commandMap = (CommandMap)f.get(Bukkit.getServer());
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.commandMap.register(this.plugin.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            runCommand(this.execute, p);
        }
        return false;
    }

    private static void runCommand(String command, Player p) {
        if (command.contains("{player}"))
            command = command.replace("{player}", p.getName());
        if (command.contains("{location}"))
            command = command.replace("{location}", locationString(p.getLocation()));
        if (command.charAt(0) == '/')
            command = command.substring(1);
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
    }

    private static String locationString(Location loc) {
        String x = String.valueOf(loc.getBlockX());
        String y = String.valueOf(loc.getBlockY());
        String z = String.valueOf(loc.getBlockZ());
        return x + " " + y + " " + z;
    }
}
