package me.dawey.addons.discord;

import me.dawey.addons.Addons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;

public class Discord {
    private String webhookURL;
    private Addons plugin;

    public Discord(String webhookURL, Addons plugin) {
        this.webhookURL = webhookURL;
        this.plugin = plugin;
    }
    public void sendSystemMessage(String message) {
        sendToDiscord(message, plugin.getMainConfig().getString("discord.system-name"), Color.BLUE, "", false);
    }

    private void sendToDiscord(String message, String userName, Color color, String iconURL, boolean withAuthor) {
        Bukkit.getScheduler().runTaskAsynchronously(Addons.getInstance(), () -> {
            DiscordWebhook discordWebhook = new DiscordWebhook(webhookURL);
            discordWebhook.setUsername(userName);
            discordWebhook.setAvatarUrl("https://mc-heads.net/head/" + userName);
            if (withAuthor) {
                if (iconURL == "") {
                    discordWebhook.addEmbed((new DiscordWebhook.EmbedObject())

                            .setAuthor(userName, "", iconURL)
                            .setDescription(message)
                            .setColor(color));
                } else {
                    discordWebhook.addEmbed((new DiscordWebhook.EmbedObject())

                            .setAuthor(message, "", iconURL)
                            .setColor(color));
                }
            } else {
                discordWebhook.addEmbed((new DiscordWebhook.EmbedObject())
                        .setDescription(message)
                        .setColor(color));
            }
            try {
                discordWebhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
