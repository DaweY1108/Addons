package me.dawey.addons.chat;

import com.willfp.eco.core.items.Items;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import me.dawey.addons.chat.items.EcoEnchantsV10Support;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatPlaceholders {
    private static Addons plugin = (Addons)Addons.getPlugin(Addons.class);
    public static Component getPlaceHolders(Component message, Player player) {
        /*
        Items
         */
        ItemStack is = player.getEquipment().getItemInMainHand();

        if (plugin.isEcoEnabled()) {
            if (Items.isCustomItem(player.getEquipment().getItemInMainHand())) {
                is = Items.getCustomItem(player.getEquipment().getItemInMainHand()).getItem();
            }
        }

        is = new ItemStack(is);
        if (is.getType() != Material.AIR && message.toString().contains("[item]")) {
            is = manage(is);
            String replacement = is.getI18NDisplayName();
            replacement = "&2" + is.getAmount() + "x " + replacement;
            replacement = ChatColorManager.translateHexColorCodes(
                    ChatColorManager.colorize(
                            plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, replacement) : replacement
                    )
            );
            message = message.replaceText(TextReplacementConfig.builder()
                    .match("\\[item\\]")  // Match the pattern "[item]"
                    .replacement(
                            Component.text()  // Create a new text component
                                    .append(Component.text(replacement))  // Add the displayed text
                                    .hoverEvent(is)  // Set hover event for item tooltip
                                    .build()
                    )
                    .build());
        }


        /*
        Inventory
         */

        if (message.toString().contains("[inv]")) {
            String replacement = "";
            replacement = plugin.getMainConfig().getString("inv-replacement");
            replacement = PlaceholderAPI.setPlaceholders(player, replacement);
            replacement = ChatColorManager.hexColorize(replacement);
            message = message
                    .replaceText(TextReplacementConfig.builder()
                            .match("\\[inv\\]")
                            .replacement(
                                    Component.text()
                                            .append(Component.text(replacement))
                                            .hoverEvent(HoverEvent.showText(Component.text(ChatColorManager.hexColorize(plugin.getMainConfig().getString("inv-hover")))))
                                            .clickEvent(ClickEvent.runCommand("/addonsinvsee " + plugin.getMainConfig().getString("inv-command-code") + " " + player.getName()))
                                            .build()
                            )
                            .build());
        }


        return message;
    }

    public static ItemStack manage(ItemStack is) {
        if (plugin.isEcoEnabled()) {
            EcoEnchantsV10Support.display(is);
        }
        return is;
    }
}
