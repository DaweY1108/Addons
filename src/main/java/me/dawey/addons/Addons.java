package me.dawey.addons;

import me.dawey.addons.config.Config;
import me.dawey.addons.discord.Discord;
import me.dawey.addons.discord.DiscordWebhook;
import me.dawey.addons.luckperms.GroupChangeListener;
import me.dawey.addons.utils.Logger;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public final class Addons extends JavaPlugin {
    private static Config mainConfig;
    private static Discord discord;

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        mainConfig = new Config("config.yml");
        discord = new Discord(mainConfig.getString("discord.webhook-url"));
        discord.sendSystemMessage("A szerver elindult!");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            GroupChangeListener groupChangeListener = new GroupChangeListener(api, this);
        }
        Logger.getLogger().info("Addons successfully started up!");

    }

    public void onGroupChange(UserDataRecalculateEvent event) {

    }

    @Override
    public void onDisable() {
    }

    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Addons.class);
    }

    public static Discord getDiscord() {
        return discord;
    }

    public static Config getMainConfig() {
        return mainConfig;
    }


}
