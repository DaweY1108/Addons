package me.dawey.addons.commands;


import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import me.dawey.addons.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Date;

public class TimedCommands {
    private static Addons plugin = (Addons)Addons.getPlugin(Addons.class);

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                for (String time : TimedCommands.plugin.timedCommands.keySet()) {
                    String[] timeSplit = time.split(";");
                    int hour = Integer.parseInt(timeSplit[0]);
                    int minute = Integer.parseInt(timeSplit[1]);
                    int second = Integer.parseInt(timeSplit[2]);
                    Date date = new Date();
                    if (date.getHours() == hour && date.getMinutes() == minute && date.getSeconds() == second)
                        TimedCommands.runCommand((String)TimedCommands.plugin.timedCommands.get(time));
                }
                int playerCount = Bukkit.getOnlinePlayers().size();
                if (playerCount > 0)
                    for (String time : TimedCommands.plugin.commands.keySet()) {
                        String[] timeSplit = time.split(";");
                        int hour = Integer.parseInt(timeSplit[0]);
                        int minute = Integer.parseInt(timeSplit[1]);
                        int second = Integer.parseInt(timeSplit[2]);
                        Date date = new Date();
                        CommandData cd = (CommandData)TimedCommands.plugin.commands.get(time);
                        String condition = cd.condition;
                        String trueCommand = cd.trueCommand;
                        String falseCommand = cd.falseCommand;
                        if (TimedCommands.checkCondition(condition)) {
                            if (date.getHours() == hour && date.getMinutes() == minute && date.getSeconds() == second)
                                TimedCommands.runCommand(trueCommand);
                            continue;
                        }
                        if (date.getHours() == hour && date.getMinutes() == minute && date.getSeconds() == second)
                            TimedCommands.runCommand(falseCommand);
                    }
            }
        },  0L, 20L);
    }

    public static void runCommand(String command) {
        if (command.charAt(0) == '/')
            command = command.substring(1);
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
    }

    private static boolean checkCondition(String condition) {
        String[] conditionSplit = condition.split(" ");
        String placeHolder = conditionSplit[0];
        String operator = conditionSplit[1];
        String number = conditionSplit[2];
        Player player = null;
        for (Player p : Bukkit.getOnlinePlayers())
            player = p;
        String placeholderData = PlaceholderAPI.setPlaceholders(player, placeHolder);
        if (isNumeric(placeholderData)) {
            int placeholderDataInt = Integer.parseInt(placeholderData);
            int numberInt = Integer.parseInt(number);
            switch (operator) {
                case ">":
                    return (placeholderDataInt > numberInt);
                case "<":
                    return (placeholderDataInt < numberInt);
                case ">=":
                    return (placeholderDataInt >= numberInt);
                case "<=":
                    return (placeholderDataInt <= numberInt);
                case "==":
                    return (placeholderDataInt == numberInt);
                case "!=":
                    return (placeholderDataInt != numberInt);
            }
        } else {
            Logger.getLogger().warn("The placeholder data is not a number!");
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null)
            return false;
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
