package me.dawey.addons.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener, ChatRenderer {

    private Addons plugin;

    public ChatListener(Addons plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        event.renderer(this); // Tell the event to use our renderer
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        String format = plugin.getMainConfig().getString("chat-format");
        format = ChatColorManager.translateHexColorCodes(
          ChatColorManager.colorize(
                  plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(source, format) : format
          )
        );
        return Component.text(format)
                .append(getPlaceHolders(message, source));
    }

    private Component getPlaceHolders(Component message, Player player) {
        ItemStack is = player.getItemInHand();

        if (is.getType() != Material.AIR && message.toString().contains("[item]")) {
            String replacement = is.getI18NDisplayName();
            replacement = "&2" + is.getAmount() + "x " + replacement;
            replacement = ChatColorManager.translateHexColorCodes(
                    ChatColorManager.colorize(
                            plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, replacement) : replacement
                    )
            );
            Bukkit.broadcastMessage(replacement);
            message = message.replaceText(TextReplacementConfig.builder()
                    .match("\\[item\\]")
                    .replacement(
                            Component.text()
                                    .append(Component.text(replacement))
                                    .hoverEvent(HoverEvent.showItem(is.asHoverEvent().value()))
                                    .build()
                    )
                    .build());
        }
        return message;
    }
}