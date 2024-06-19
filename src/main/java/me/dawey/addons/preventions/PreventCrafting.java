package me.dawey.addons.preventions;

import me.dawey.addons.Addons;
import me.dawey.addons.chat.ChatColorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PreventCrafting implements Listener {
    private Addons plugin;
    List<String> blockedRecipes;

    public PreventCrafting(Addons plugin) {
        this.plugin = plugin;
        this.blockedRecipes = this.plugin.getMainConfig().getStringList("blocked-crafting");
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        String resultName = event.getRecipe().getResult().getType().name();
        if (!blockedRecipes.contains(resultName)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        if (player.hasPermission("addons.bypass.crafting")) {
            return;
        }
        if (player.hasPermission("addons.can-craft." + resultName)) {
            return;
        }
        event.setCancelled(true);
        String msg = plugin.getChatPrefix() + plugin.getMainConfig().getString("not-craftable");
        player.sendMessage(ChatColorManager.translateHexColorCodes(ChatColorManager.colorize(msg)));
    }
}
