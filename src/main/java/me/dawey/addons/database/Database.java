package me.dawey.addons.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.dawey.addons.Addons;
import me.dawey.addons.config.Config;
import me.dawey.addons.database.entites.SocialData;
import me.dawey.addons.utils.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {
    private Dao<SocialData, String> discordConnDataDao;
    private Addons plugin;

    public Database(Addons plugin) throws SQLException {
        this.plugin = plugin;
    }

    public void initTables() {
        ConnectionSource connectionSource = getConnectionSource();
        try {
            TableUtils.createTableIfNotExists(connectionSource, SocialData.class);
            discordConnDataDao = DaoManager.createDao(connectionSource, SocialData.class);
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to create tables in database! Error: " + e.getMessage());
        }
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

    public Map<String, String> getAllSocialData() {
        List<SocialData> socialDataList = null;
        Map<String, String> socialDataMap = new HashMap<>();
        try {
            for (SocialData socialData : discordConnDataDao.queryForAll()) {
                socialDataMap.put(socialData.getLOWERCASENICKNAME(), socialData.getDISCORD_ID() + "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socialDataMap;
    }

    public String getDiscordId(String nickname) {
        try {
            SocialData socialData = discordConnDataDao.queryForId(nickname.toLowerCase());
            return socialData.getDISCORD_ID() + "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isUserConnected(Long discordId) {
        try {
            return discordConnDataDao.queryForEq("DISCORD_ID", discordId).size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPlayerName(Long discordId) {
        try {
            SocialData socialData = discordConnDataDao.queryForEq("DISCORD_ID", discordId).get(0);
            return socialData.getLOWERCASENICKNAME();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
