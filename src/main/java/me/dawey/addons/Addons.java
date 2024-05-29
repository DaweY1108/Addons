package me.dawey.addons;

import me.dawey.addons.config.Config;
import me.dawey.addons.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Addons extends JavaPlugin {
    private static Config config;

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        config = new Config("config.yml");
        Logger.getLogger().info("Addons successfully started up!");

    }

    @Override
    public void onDisable() {
    }

    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Addons.class);
    }


}
