package me.dawey.addons.discord;

import me.dawey.addons.Addons;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;

public class Discord {
    private String webhookURL;
    private Addons plugin;

    public Discord(Addons plugin) {
        this.plugin = plugin;
        this.webhookURL = plugin.getDiscordConfig().getString("webhook.url");
    }

    public void sendPlayerMessage(Player player, String message) {
        message = replaceCodes(message);
        sendToDiscord(message, player.getName(), Color.DARK_GRAY, "", false);
    }
    public void sendSystemMessage(String message) {
        sendToDiscord(message, plugin.getDiscordConfig().getString("webhook.system-name"), Color.BLUE, plugin.getDiscordConfig().getString("webhook.system-avatar"), false);
    }

    public void sendEmojiDiscord(ConfigurationSection section) {
        Bukkit.getScheduler().runTaskAsynchronously(Addons.getInstance(), () -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            DiscordWebhook discordWebhook = new DiscordWebhook(webhookURL);
            discordWebhook.setUsername(plugin.getDiscordConfig().getString("webhook.system-name"));
            String avatarURL = section.getString("avatar").equalsIgnoreCase("global") ? plugin.getDiscordConfig().getString("webhook.system-avatar") : section.getString("avatar");
            discordWebhook.setAvatarUrl(avatarURL);
            // Create an embed object
            DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject()
                    .setTitle(section.getString("title"))  // Set the title
                    .setDescription(section.getString("message"))  // Set the description
                    .setColor(Color.decode(section.getString("color")))  // Set the color
                    .setFooter(LocalDateTime.now().format(formatter), null);  // Set footer text and time

            discordWebhook.addEmbed(embed);

            try {
                discordWebhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    public String replaceCodes(String text) {
        if (text.contains("\"")) {
            text = text.replace("\"", "\\" + "\"");
        }
        if (text.contains("\n")) {
            text = text.replace("\n", "\\n");
        }
        if (text.contains("\r")) {
            text = text.replace("\r", "\\r");
        }
        if (text.contains("\t")) {
            text = text.replace("\t", "\\t");
        }
        if (text.contains("\b")) {
            text = text.replace("\b", "\\b");
        }
        if (text.contains("\f")) {
            text = text.replace("\f", "\\f");
        }
        if (text.contains("\\")) {
            text = text.replace("\\", "\\\\");
        }
        return text;
    }


}
