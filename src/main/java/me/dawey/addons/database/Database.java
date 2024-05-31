package me.dawey.addons.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.dawey.addons.database.entites.SocialData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private final Dao<SocialData, String> discordConnDataDao;

    public Database(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, SocialData.class);
        discordConnDataDao = DaoManager.createDao(connectionSource, SocialData.class);
    }

    public Map<String, String> getAllSocialData() {
        List<SocialData> socialDataList = null;
        Map<String, String> socialDataMap = new HashMap<>();
        try {
            socialDataList = discordConnDataDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (SocialData socialData : socialDataList) {
            socialDataMap.put(socialData.getLOWERCASENICKNAME(), socialData.getDISCORD_ID() + "");
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
}
