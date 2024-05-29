package me.dawey.addons;

import me.dawey.addons.Utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Addons extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");


        Logger.getLogger().info("Addons successfully started up!");

    }

    @Override
    public void onDisable() {
    }


}
