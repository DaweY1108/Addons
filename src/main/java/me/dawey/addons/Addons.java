package me.dawey.addons;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import me.dawey.addons.config.Config;
import me.dawey.addons.database.Database;
import me.dawey.addons.discord.Discord;
import me.dawey.addons.discord.DiscordBot;
import me.dawey.addons.luckperms.GroupChangeListener;
import me.dawey.addons.utils.Logger;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.IOException;

public final class Addons extends JavaPlugin {
    private static Config mainConfig;
    private static Config discordConfig;
    private static Config databaseConfig;

    private static Database database;
    private static Discord discord;
    private static DiscordBot discordBot;

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        loadConfig();
        initDatabase();
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
        databaseConfig = new Config("database.yml");
    }

    private void initDiscord() {
        Logger.getLogger().info("Initializing Discord Webhook...");
        discord = new Discord(this);
        Logger.getLogger().info("Starting Discord Bot...");
        discordBot = new DiscordBot(this);
        discordBot.start();
        discordBot.sendSystemMessage("A szerver elindult!");
    }

    private void initDatabase() {
        Logger.getLogger().info("Initializing database...");
        String dbType = databaseConfig.getString("database.type");
        ConnectionSource connectionSource = null;
        switch (dbType) {
            case "sqlite":
                try {
                    connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "/addons.db");
                    database = new Database(connectionSource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "mysql":
                try {
                    connectionSource = new JdbcConnectionSource("jdbc:mysql://" + databaseConfig.getString("database.host") + ":" + databaseConfig.getString("database.port") + "/" + databaseConfig.getString("database.database"), databaseConfig.getString("database.username"), databaseConfig.getString("database.password"));
                    database = new Database(connectionSource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                Logger.getLogger().warn("Invalid database type in database.yml!");
                break;
        }
        for (String name : database.getAllSocialData().keySet()) {
            Logger.getLogger().info(name + " - " + database.getAllSocialData().get(name));
        }
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

    public Database getDatabase() {
        return database;
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public Config getDiscordConfig() {
        return discordConfig;
    }


}
