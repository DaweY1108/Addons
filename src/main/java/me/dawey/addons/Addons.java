package me.dawey.addons;

import me.dawey.addons.chat.ChatListener;
import me.dawey.addons.commands.CommandData;
import me.dawey.addons.commands.CustomCommand;
import me.dawey.addons.commands.StartCommands;
import me.dawey.addons.commands.TimedCommands;
import me.dawey.addons.config.Config;
import me.dawey.addons.database.Database;
import me.dawey.addons.discord.Discord;
import me.dawey.addons.inventory.FixedItem;
import me.dawey.addons.inventory.Stashes;
import me.dawey.addons.player.PlayerListener;
import me.dawey.addons.plugincommands.AddonsCommand;
import me.dawey.addons.preventions.ItemProtection;
import me.dawey.addons.preventions.PreventCrafting;
import me.dawey.addons.utils.Announces;
import me.dawey.addons.inventory.InventoryCheck;
import me.dawey.addons.utils.Logger;
import me.dawey.addons.vendor.PapiPlaceholders;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class Addons extends JavaPlugin {
    private static Config mainConfig;
    private static Config discordConfig;
    private static Config databaseConfig;
    private static Config announceConfig;
    public Map<String, CommandData> commands = new HashMap<>();
    public Map<String, String> timedCommands = new HashMap<>();
    private Map<String, CustomCommand> customCommandsMap = new HashMap<>();
    public static Database database;
    private static Discord discord;
    private Announces announces;

    @Override
    public void onEnable() {
        Logger.getLogger().info("Starting up addons...");
        loadConfig();
        initDatabase();
        initListeners();
        initCommands();
        initPlaceholderAPI();
        initAnnounces();
        initStashes();
        initDiscord();
        initPreventions();
        Logger.getLogger().info("Addons successfully started up!");
        discord.sendEmojiDiscordAsync(getDiscordConfig().getConfigurationSection("messages.start"), null);
    }

    @Override
    public void onDisable() {
        Logger.getLogger().info("Shutting down addons...");
        Logger.getLogger().info("Addons successfully shut down!");
        discord.sendEmojiDiscordSync(getDiscordConfig().getConfigurationSection("messages.stop"), null);
    }

    private void loadConfig() {
        Logger.getLogger().info("Loading config files...");
        mainConfig = new Config("config.yml");
        discordConfig = new Config("discord/config.yml");
        databaseConfig = new Config("database.yml");
        announceConfig = new Config("announces.yml");
    }

    private void initDiscord() {
        Logger.getLogger().info("Initializing Discord Webhook...");
        discord = new Discord(this);
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

        Bukkit.getPluginCommand("addons").setExecutor(new AddonsCommand(this));
        TimedCommands.start();
        StartCommands.start();
    }

    private void initPlaceholderAPI() {
        (new PapiPlaceholders(this)).register();
    }

    private void initAnnounces() {
        Logger.getLogger().info("Initializing announces...");
        announces = new Announces(this);
    }

    private void initStashes() {
        if (getMainConfig().getBoolean("stashes-enabled")) {
            Logger.getLogger().info("Initializing stashes...");
            Stashes stashes = new Stashes(this);
            Bukkit.getPluginManager().registerEvents(stashes, this);
            Bukkit.getPluginCommand("stashes").setExecutor(stashes);
        }
    }

    private void initPreventions() {
        PreventCrafting preventCrafting = new PreventCrafting(this);
    }

    private void initDatabase() {
        Logger.getLogger().info("Initializing database...");
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
        try {
            database = new Database(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PreventCrafting(this), this);
        InventoryCheck inventoryCheck = new InventoryCheck(this);
        Bukkit.getPluginCommand("addonsinvsee").setExecutor(inventoryCheck);
        Bukkit.getPluginManager().registerEvents(inventoryCheck, this);
        Bukkit.getPluginManager().registerEvents(new ItemProtection(this), this);
        ConfigurationSection fixedItemSection = getMainConfig().getConfigurationSection("fixed-items");
        if (fixedItemSection != null) {
            FixedItem fixedItem = new FixedItem(this, fixedItemSection);
            Bukkit.getPluginManager().registerEvents(fixedItem, this);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }


    public void reload() {
        loadConfig();
        initCommands();
        initPlaceholderAPI();
        initAnnounces();
        initStashes();
        initDiscord();
    }
    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Addons.class);
    }

    public boolean isEcoEnabled () {
        return Bukkit.getPluginManager().isPluginEnabled("eco");
    }

    public String getChatPrefix() {
        return mainConfig.getString("chat-prefix");
    }

    public Discord getDiscord() {
        return discord;
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

    public Config getAnnounceConfig() {
        return announceConfig;
    }


}
