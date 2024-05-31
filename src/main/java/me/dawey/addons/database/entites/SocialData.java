package me.dawey.addons.database.entites;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "SOCIAL")
public class SocialData {
    @DatabaseField(id = true, columnName = "LOWERCASENICKNAME")
    private String LOWERCASENICKNAME;
    @DatabaseField(columnName = "VK_ID")
    private long VK_ID;
    @DatabaseField(columnName = "TELEGRAM_ID")
    private long TELEGRAM_ID;
    @DatabaseField(columnName = "DISCORD_ID")
    private long DISCORD_ID;
    @DatabaseField(columnName = "BLOCKED")
    private byte BLOCKED;
    @DatabaseField(columnName = "TOTP_ENABLED")
    private byte TOTP_ENABLED;
    @DatabaseField(columnName = "NOTIFY_ENABLED")
    private byte NOTIFY_ENABLED;

    public SocialData() {}

    public SocialData(String LOWERCASENICKNAME, long VK_ID, long TELEGRAM_ID, long DISCORD_ID, byte BLOCKED, byte TOTP_ENABLED, byte NOTIFY_ENABLED) {
        this.LOWERCASENICKNAME = LOWERCASENICKNAME;
        this.VK_ID = VK_ID;
        this.TELEGRAM_ID = TELEGRAM_ID;
        this.DISCORD_ID = DISCORD_ID;
        this.BLOCKED = BLOCKED;
        this.TOTP_ENABLED = TOTP_ENABLED;
        this.NOTIFY_ENABLED = NOTIFY_ENABLED;
    }

    public String getLOWERCASENICKNAME() {
        return LOWERCASENICKNAME;
    }

    public long getVK_ID() {
        return VK_ID;
    }

    public long getTELEGRAM_ID() {
        return TELEGRAM_ID;
    }

    public long getDISCORD_ID() {
        return DISCORD_ID;
    }

    public byte getBLOCKED() {
        return BLOCKED;
    }

    public byte getTOTP_ENABLED() {
        return TOTP_ENABLED;
    }

    public byte getNOTIFY_ENABLED() {
        return NOTIFY_ENABLED;
    }

    public void setLOWERCASENICKNAME(String LOWERCASENICKNAME) {
        this.LOWERCASENICKNAME = LOWERCASENICKNAME;
    }

    public void setVK_ID(long VK_ID) {
        this.VK_ID = VK_ID;
    }

    public void setTELEGRAM_ID(long TELEGRAM_ID) {
        this.TELEGRAM_ID = TELEGRAM_ID;
    }

    public void setDISCORD_ID(long DISCORD_ID) {
        this.DISCORD_ID = DISCORD_ID;
    }

    public void setBLOCKED(byte BLOCKED) {
        this.BLOCKED = BLOCKED;
    }

    public void setTOTP_ENABLED(byte TOTP_ENABLED) {
        this.TOTP_ENABLED = TOTP_ENABLED;
    }

    public void setNOTIFY_ENABLED(byte NOTIFY_ENABLED) {
        this.NOTIFY_ENABLED = NOTIFY_ENABLED;
    }
}
