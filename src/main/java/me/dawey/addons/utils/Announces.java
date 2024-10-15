package me.dawey.addons.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

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
        int a = 0;
        Set<String> announces = plugin.getAnnounceConfig().getConfigurationSection("announces").getKeys(false);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if (index.get() >= announces.size()) {
                index.set(0);
            }
            List<String> announce = plugin.getAnnounceConfig().getStringList("announces." + announces.toArray()[index.getAndIncrement()]);
            announce(announce);
        }, 0L, 20L * plugin.getMainConfig().getInt("announce-interval"));
    }
    public void announce(List<String> message) {
        Bukkit.getOnlinePlayers().forEach(p -> message.forEach(msg -> {
            msg = PlaceholderAPI.setPlaceholders(p, msg);
            Component component = MiniMessage.miniMessage().deserialize(msg);
            p.sendMessage(component);
        }));
    }
}
