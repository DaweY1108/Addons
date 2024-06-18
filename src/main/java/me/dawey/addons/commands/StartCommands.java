package me.dawey.addons.commands;

import me.dawey.addons.Addons;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class StartCommands {
    private static Addons plugin = (Addons)Addons.getPlugin(Addons.class);

    public static void start() {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)plugin, new Runnable() {
            public void run() {
                for (String s : StartCommands.plugin.getMainConfig().getStringList("start-commands"))
                    TimedCommands.runCommand(s);
            }
        },  1800L);
    }
}
