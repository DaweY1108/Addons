package me.dawey.addons.Utils;

import org.bukkit.Bukkit;

public class Logger {
    private static final String prefix = "[Addons] ";
    private static final Log logger = new Log();
    public static class Log {
        public void info(String message) {
            Bukkit.getLogger().info(prefix + message);
        }

        public void warn(String message) {
            Bukkit.getLogger().warning(prefix + message);
        }

        public void system(String message) {
            Bukkit.getLogger().info(message);
        }
    }

    public static Log getLogger() {
        return logger;
    }
}
