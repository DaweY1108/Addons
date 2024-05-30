package me.dawey.addons.discord;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscordBot {

    private String token;
    private JDA bot;
    private Addons plugin;
    public DiscordBot(String token, Addons plugin) {
        this.token = token;
        this.plugin = plugin;
    }

    public void start() {
        bot = JDABuilder.createDefault(token).build();
        try {
            bot.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        bot.shutdown();
    }

    public void restart() {
        stop();
        start();
    }

    public void descriptions() {
        AtomicInteger index = new AtomicInteger(0);
        List<String> descriptions = plugin.getDiscordConfig().getStringList("bot.descriptions");

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if (index.get() >= descriptions.size()) {
                index.set(0); // Reset index if it's out of bounds
            }
            String currentDescription = descriptions.get(index.getAndIncrement());
            currentDescription = PlaceholderAPI.setPlaceholders(null, currentDescription);
            bot.getPresence().setActivity(Activity.playing(currentDescription));
        }, 0L, 20L * 30L);
    }

    public void sendSystemMessage(String message) {
        Bukkit.getScheduler().runTaskAsynchronously(Addons.getInstance(), () -> {
            TextChannel channel = bot.getTextChannelById("1233907158773399644");
            channel.sendMessage(message).queue();
        });
    }
}
