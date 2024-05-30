package me.dawey.addons;

import me.dawey.addons.config.Config;
import me.dawey.addons.discord.Discord;
import me.dawey.addons.discord.DiscordBot;
import me.dawey.addons.luckperms.GroupChangeListener;
import me.dawey.addons.utils.Logger;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public final class Addons extends JavaPlugin {
    private static Config mainConfig;
    private static Config discordConfig;
    private static Discord discord;
    private static DiscordBot discordBot;

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        loadConfig();
        initDiscord();
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            GroupChangeListener groupChangeListener = new GroupChangeListener(api, this);
        }
        Logger.getLogger().info("Addons successfully started up!");

    }

    private void loadConfig() {
        Logger.getLogger().info("Loading config files...");
        mainConfig = new Config("config.yml");
        discordConfig = new Config("discord/config.yml");
    }

    private void initDiscord() {
        Logger.getLogger().info("Initializing Discord Webhook...");
        discord = new Discord(this);

        Logger.getLogger().info("Starting Discord Bot...");
        discordBot = new DiscordBot(this);
        discordBot.start();
        discordBot.descriptions();
        discordBot.sendSystemMessage("A szerver elindult!");
        discordBot.getUsers().forEach(user ->Logger.getLogger().info(user));

    }

    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Addons.class);
    }

    public Discord getDiscord() {
        return discord;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public Config getDiscordConfig() {
        return discordConfig;
    }


}
