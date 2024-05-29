package me.dawey.addons;

import me.dawey.addons.config.Config;
import me.dawey.addons.discord.Discord;
import me.dawey.addons.discord.DiscordWebhook;
import me.dawey.addons.utils.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public final class Addons extends JavaPlugin {
    private static Config mainConfig;
    private static DiscordWebhook discordWebhook;

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        mainConfig = new Config("config.yml");
        discordWebhook = new DiscordWebhook(mainConfig.getString("discord.webhook-url"));
        Discord.sendSystemMessage("A szerver elindult!");
        Logger.getLogger().info("Addons successfully started up!");

    }

    @Override
    public void onDisable() {
    }

    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Addons.class);
    }

    public static DiscordWebhook getDiscordWebhook() {
        return discordWebhook;
    }

    public static Config getMainConfig() {
        return mainConfig;
    }


}
