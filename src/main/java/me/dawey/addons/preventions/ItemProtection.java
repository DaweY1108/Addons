package me.dawey.addons.preventions;

import me.dawey.addons.Addons;
import me.dawey.addons.chat.ChatColorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemProtection implements Listener {

    private Addons plugin;
    private final List<UUID> cancelDoubleMessage;

    public ItemProtection(Addons plugin) {
        this.plugin = plugin;
        this.cancelDoubleMessage = new ArrayList<>();
    }

    /*
    Anvil
     */
    @EventHandler
    public void anvilClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) return;

        Player player = (Player) human;
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof AnvilInventory)) return;

        InventoryView inventoryView = event.getView();
        int slot = event.getRawSlot();
        if (slot != inventoryView.convertSlot(slot)) return;
        if (slot != 2) return;

        ProtectedItem item = new ProtectedItem(inventory.getItem(0), plugin);
        ProtectedItem mergedWith = new ProtectedItem(inventory.getItem(1), plugin);
        ProtectedItem result = new ProtectedItem(inventory.getItem(2), plugin);

        boolean involvesLockedItem = false;
        boolean isRepair = false;
        boolean isChangeInEnchantments = false;

        if (item == null) return;
        if (result == null) return;
        if (item.isProtected()) involvesLockedItem = true;
        if (result.isProtected()) involvesLockedItem = true;

        if (mergedWith != null) {
            if (mergedWith.isProtected()) involvesLockedItem = true;
            if (mergedWith.getType() != Material.ENCHANTED_BOOK) {
                isRepair = true;
            }
            if (item.getEnchantments() != result.getEnchantments()) {
                isRepair = false;
                isChangeInEnchantments = true;
            }
        }

        if (!involvesLockedItem) return;

        if (isChangeInEnchantments) {
            event.setCancelled(true);
            player.sendMessage(ChatColorManager.hexColorize(plugin.getChatPrefix() + plugin.getMainConfig().getString("item-protected")));
            return;
        }

        if (isRepair) {
            event.setCancelled(true);
            player.sendMessage(ChatColorManager.hexColorize(plugin.getChatPrefix() + plugin.getMainConfig().getString("item-protected")));
        }
    }

    /*
    Enchant table
     */

    @EventHandler
    public void onEnchantmentTable(PrepareItemEnchantEvent event) {
        ProtectedItem item = new ProtectedItem(event.getItem(), plugin);
        if (!item.isProtected()) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getEnchanter();
        UUID uuid = player.getUniqueId();

        // This event is called multiple times for the same enchantment.
        // This prevents the chat of players from being spammed in that case.
        if (!cancelDoubleMessage.contains(uuid)) {
            player.sendMessage(ChatColorManager.hexColorize(plugin.getChatPrefix() + plugin.getMainConfig().getString("item-protected")));
            cancelDoubleMessage.add(uuid);
            removeInTwoTicks(uuid);
        }
    }

    /*
    GrindStone
     */
    @EventHandler
    public void grindStoneClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player))	return;

        Player player = (Player) human;
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof GrindstoneInventory)) return;

        InventoryView inventoryView = event.getView();
        int slot = event.getRawSlot();
        if (slot != inventoryView.convertSlot(slot)) return;
        if (slot != 2) return;

        ProtectedItem upperItem = new ProtectedItem(inventory.getItem(0), plugin);
        ProtectedItem lowerItem = new ProtectedItem(inventory.getItem(1), plugin);
        ProtectedItem resultItem = new ProtectedItem(inventory.getItem(2), plugin);

        boolean involvesLockedItem = false;

        if (upperItem != null) {
            if (upperItem.isProtected()) involvesLockedItem = true;
        }

        if (lowerItem != null) {
            if (lowerItem.isProtected()) involvesLockedItem = true;
        }

        if (resultItem != null) {
            if (resultItem.isProtected()) involvesLockedItem = true;
        }

        if (!involvesLockedItem) return;

        event.setCancelled(true);
        player.sendMessage(ChatColorManager.hexColorize(plugin.getChatPrefix() + plugin.getMainConfig().getString("item-protected")));
    }

    /*
    Smithing
     */

    @EventHandler
    public void smithingClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) return;

        Player player = (Player) human;
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof SmithingInventory)) return;

        InventoryView inventoryView = event.getView();
        int slot = event.getRawSlot();
        if (slot != inventoryView.convertSlot(slot)) return;
        if (slot != 3) return;

        ProtectedItem item = new ProtectedItem(inventory.getItem(0), plugin);
        ProtectedItem mergedWith = new ProtectedItem(inventory.getItem(1), plugin);
        ProtectedItem result = new ProtectedItem(inventory.getItem(3), plugin);

        boolean involvesLockedItem = false;

        if (item == null) return;
        if (result == null) return;
        if (mergedWith == null) return;

        if (item.isProtected()) involvesLockedItem = true;
        if (result.isProtected()) involvesLockedItem = true;
        if (mergedWith.isProtected()) involvesLockedItem = true;

        if (!involvesLockedItem) return;

        event.setCancelled(true);
        player.sendMessage(ChatColorManager.hexColorize(plugin.getChatPrefix() + plugin.getMainConfig().getString("item-protected")));
    }

    /**
     * Schedules the removal from a UUID from the cancelDoubleMessage list.
     * @param uuid The UUID to remove.
     */
    private void removeInTwoTicks(UUID uuid) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskLater(plugin, () -> cancelDoubleMessage.remove(uuid), 2L);
    }
}
