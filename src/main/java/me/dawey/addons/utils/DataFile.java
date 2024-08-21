package me.dawey.addons.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataFile extends YamlConfiguration {
    private File file;
    private String defaults;
    private JavaPlugin plugin;
    public DataFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    //Adatfile készítése
    public DataFile(JavaPlugin plugin, String fileName, String defaultsname) {
        this.plugin = plugin;
        this.defaults = defaultsname;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    //Adatfile legenerálása és/vagy újratöltése a "DataFile" alapján
    public void reload() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
                Bukkit.getLogger().warning("Hiba lepett fel a file keszitese kozben!");
            }
        }
        try {
            load(file);

            if (defaults != null) {
                InputStreamReader reader = new InputStreamReader(plugin.getResource(defaults));
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                setDefaults(defaultsConfig);
                options().copyDefaults(true);

                reader.close();
                save();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            //plugin.getLogger().severe("Error while loading file " + file.getName());

        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            //plugin.getLogger().severe("Error while loading file " + file.getName());

        }
    }

    //Adatfile mentése
    public void save() {

        try {
            options().indent(2);
            save(file);

        } catch (IOException exception) {
            exception.printStackTrace();
            //plugin.getLogger().severe("Error while saving file " + file.getName());
        }

    }
}
