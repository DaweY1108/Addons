package me.dawey.addons.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.willfp.eco.core.Eco;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.items.CustomItem;
import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.items.builder.EnchantedBookBuilder;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import com.willfp.ecoenchants.enchant.impl.LibreforgeEcoEnchant;
import com.willfp.ecoitems.items.EcoItems;
import com.willfp.ecoitems.slot.ItemSlot;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.libs.kyori.adventure.text.TextComponent;
import me.clip.placeholderapi.libs.kyori.adventure.text.event.HoverEventSource;
import me.dawey.addons.Addons;
import me.dawey.addons.chat.items.EcoEnchantsV10Support;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.lang.reflect.Modifier;
import java.util.*;

public class ChatPlaceholders {
    private static Addons plugin = (Addons)Addons.getPlugin(Addons.class);
    public static Component getPlaceHolders(Component message, Player player) {
        /*
        Items
         */
        ItemStack is = player.getEquipment().getItemInMainHand();

        if (Items.isCustomItem(player.getEquipment().getItemInMainHand())) {
            is = Items.getCustomItem(player.getEquipment().getItemInMainHand()).getItem();
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
        return message;
    }

    public static ItemStack manage(ItemStack is) {
        EcoEnchantsV10Support.display(is);
        return is;
    }
}
