package me.dawey.addons.config;

import me.dawey.addons.Addons;
import me.dawey.addons.utils.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Config extends YamlConfiguration {
    private String fileName = "";
    private JavaPlugin plugin;

    public Config(String fileName) {
        this.fileName = fileName;
        this.plugin = Addons.getInstance();
        loadConfig();
    }

    public boolean loadConfig() {
        File f = new File(plugin.getDataFolder(), fileName);
        if (!f.exists()) {
            InputStream is = plugin.getResource(fileName);
            try {
                FileUtils.copyInputStreamToFile(is, new File(plugin.getDataFolder(), fileName));
            } catch (IOException e) {
                Logger.getLogger().warn("Couldn't get \"" + fileName +"\" config. Error message: " + e.getMessage());
                return false;
            }
        }
        try {
            this.load(f);
        } catch (Exception ex) {
            Logger.getLogger().warn("Couldn't load \"" + fileName +"\" config. Please check for errors in the file. Error message: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean reloadConfig() {
        return loadConfig();
    }
}
