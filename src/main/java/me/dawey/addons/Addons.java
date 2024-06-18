package me.dawey.addons;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import me.dawey.addons.chat.ChatListener;
import me.dawey.addons.commands.CommandData;
import me.dawey.addons.commands.CustomCommand;
import me.dawey.addons.commands.StartCommands;
import me.dawey.addons.commands.TimedCommands;
import me.dawey.addons.config.Config;
import me.dawey.addons.database.Database;
import me.dawey.addons.discord.Discord;
import me.dawey.addons.discord.DiscordBot;
import me.dawey.addons.luckperms.GroupChangeListener;
import me.dawey.addons.utils.Logger;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class Addons extends JavaPlugin {
    private static Config mainConfig;
    private static Config discordConfig;
    private static Config databaseConfig;
    public Map<String, CommandData> commands = new HashMap<>();
    public Map<String, String> timedCommands = new HashMap<>();
    private Map<String, CustomCommand> customCommandsMap = new HashMap<>();
    public static Database database;
    private static Discord discord;
    private static DiscordBot discordBot;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(net.dv8tion.jda.internal.requests.WebSocketClient.class);

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        loadConfig();
        initDatabase();
        initListeners();
        initCommands();
        /*
        initDiscord();
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            GroupChangeListener groupChangeListener = new GroupChangeListener(api, this);
        }
         */
        Logger.getLogger().info("Addons successfully started up!");

    }

    @Override
    public void onDisable() {
        Logger.getLogger().info("Shutting down addons...");
        // discordBot.stop();
        Logger.getLogger().info("Addons successfully shut down!");
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
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                discordBot.start();
                discordBot.sendSystemMessage("Addons elindult! Discord összekötések száma:" + database.getAllSocialData().size());
            }
        );
    }

    private void initCommands() {
        this.timedCommands.clear();
        this.commands.clear();
        for (String s : getMainConfig().getConfigurationSection("timed-commands").getKeys(false))
            this.timedCommands.put(s, getMainConfig().getString("timed-commands." + s));
        for (String s : getMainConfig().getConfigurationSection("timed-commands-if").getKeys(false)) {
            ConfigurationSection section = getMainConfig().getConfigurationSection("timed-commands-if." + s);
            String condition = section.getString("condition");
            String trueCommand = section.getString("true");
            String falseCommand = section.getString("false");
            CommandData cd = new CommandData(condition, trueCommand, falseCommand);
            this.commands.put(s, cd);
        }
        if (!this.customCommandsMap.isEmpty())
            this.customCommandsMap.clear();
        for (String command : getMainConfig().getConfigurationSection("commands").getKeys(false))
            this.customCommandsMap.put(command, new CustomCommand(command, getMainConfig().getString("commands." + command)));

        TimedCommands.start();
        StartCommands.start();
    }

    private void initDatabase() {
        Logger.getLogger().info("Initializing database...");
        System.setProperty("com.j256.ormlite.logger.type", "LOCAL");
        System.setProperty("com.j256.ormlite.logger.level", "WARN");
        try {
            database = new Database(this);
            database.initTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
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

    public Config getDatabaseConfig() {
        return databaseConfig;
    }


}
