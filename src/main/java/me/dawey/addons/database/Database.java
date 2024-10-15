package me.dawey.addons.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.dawey.addons.Addons;
import me.dawey.addons.config.Config;
import me.dawey.addons.utils.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {
    private Addons plugin;

    public Database(Addons plugin) throws SQLException {
        this.plugin = plugin;
    }

    public ConnectionSource getConnectionSource() {
        Config databaseConfig = plugin.getDatabaseConfig();
        String dbType = databaseConfig.getString("database.type");
        switch (Objects.requireNonNull(dbType)) {
            case "sqlite":
                try {
                    return new JdbcConnectionSource("jdbc:sqlite:" + Addons.getInstance().getDataFolder().getAbsolutePath() + "/addons.db");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            case "mysql":
                try {
                    return new JdbcConnectionSource("jdbc:mysql://" + databaseConfig.getString("database.host") + ":" + databaseConfig.getString("database.port") + "/" + databaseConfig.getString("database.database"), databaseConfig.getString("database.username"), databaseConfig.getString("database.password"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            default:
                Logger.getLogger().warn("Invalid database type in database.yml!");
                return null;
        }
    }
}
