package me.dawey.addons.player;


import me.dawey.addons.Addons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private Addons plugin;

    public PlayerListener(Addons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getDiscord().sendEmojiDiscordAsync(plugin.getDiscordConfig().getConfigurationSection("messages.join"), event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        plugin.getDiscord().sendEmojiDiscordAsync(plugin.getDiscordConfig().getConfigurationSection("messages.leave"), event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        plugin.getDiscord().sendEmojiDiscordAsync(plugin.getDiscordConfig().getConfigurationSection("messages.death"), event.getPlayer());
    }
}
