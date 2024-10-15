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
            CommandExecutor.runCommand(this.execute, p, false);
        }
        return false;
    }
}
