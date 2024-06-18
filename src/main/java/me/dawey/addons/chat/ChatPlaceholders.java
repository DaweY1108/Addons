package me.dawey.addons.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatPlaceholders {
    private static Addons plugin = (Addons)Addons.getPlugin(Addons.class);
    public static Component getPlaceHolders(Component message, Player player) {
        /*
        Items
         */
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

        /*
        Inventory
         */
        if (message.toString().contains("[inv]")) {
            String replacement = "";
            replacement = plugin.getMainConfig().getString("inv-replacement").replace("{player}", player.getName());
            replacement = PlaceholderAPI.setPlaceholders(player, replacement);
            replacement = ChatColorManager.colorize(ChatColorManager.translateHexColorCodes(replacement));
            TextComponent comp = (((Component.text().append(Component.text(replacement)))
                .hoverEvent(HoverEvent.showText(Component.text(
                    ChatColorManager.colorize(ChatColorManager.translateHexColorCodes(plugin.getMainConfig().getString("inv-hover")))))
                )
            ).clickEvent(ClickEvent.runCommand("/addonsinvsee " + plugin.getMainConfig().getString("inv-command-code") + " " + player.getName()))).build();
            message = message.replaceText(TextReplacementConfig.builder()
                    .match("\\[inv\\]")
                    .replacement(comp)
                    .build());
        }




        return message;
    }
}
