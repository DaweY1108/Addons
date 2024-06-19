package me.dawey.addons.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import me.dawey.addons.chat.ChatColorManager;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Announces {

    private Addons plugin;

    public Announces(Addons plugin) {
        this.plugin = plugin;
        startAnnounces();
    }
    public void startAnnounces() {
        AtomicInteger index = new AtomicInteger(0);
        Set<String> announces = plugin.getAnnounceConfig().getConfigurationSection("announces").getKeys(false);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if (index.get() >= announces.size()) {
                index.set(0); // Reset index if it's out of bounds
            }
            List<String> announce = plugin.getAnnounceConfig().getStringList("announces." + announces.toArray()[index.getAndIncrement()]);
            announce(announce);
        }, 0L, 20L * 10L);
    }
    public void announce(List<String> message) {
        Bukkit.getOnlinePlayers().forEach(p -> message.forEach(msg -> {
            msg = PlaceholderAPI.setPlaceholders(p, msg);
            Component component = MiniMessage.miniMessage().deserialize(msg);
            p.sendMessage(component);
        }));
    }
}
