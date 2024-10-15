package me.dawey.addons.inventory;

import me.dawey.addons.Addons;
import me.dawey.addons.commands.CommandExecutor;
import me.dawey.addons.utils.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixedItem implements Listener {

    private Map<Integer, ItemStack> fixedItems;

    private Map<ItemStack, List<String>> itemCommands;
    private Addons plugin;

    public FixedItem(Addons plugin, ConfigurationSection config) {
        this.plugin = plugin;
        this.fixedItems = new HashMap<>();
        this.itemCommands = new HashMap<>();



        for (String key : config.getKeys(false)) {
            int slot = config.getInt(key + ".slot");
            ItemStack customItem = CustomItem.createItem(config.getConfigurationSection(key + ".item"), 1);
            itemCommands.put(customItem, config.getStringList(key + ".commands"));
            if (slot < 0 || slot > 35) {
                plugin.getLogger().warning("Invalid slot for fixed item: " + key);
                continue;
            }
            if (fixedItems.containsKey(slot)) {
                plugin.getLogger().warning("Duplicate slot for fixed item: " + key);
                continue;
            }
            fixedItems.put(slot, customItem);
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (Map.Entry<Integer, ItemStack> entry : fixedItems.entrySet()) {
                    placeItem(player, entry.getKey(), entry.getValue());
                }
            }
        }, 0, 200);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        ItemStack item = event.getCurrentItem();

        if (event.getAction() == InventoryAction.HOTBAR_SWAP) {
            item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
        }

        if (event.getAction() == InventoryAction.CLONE_STACK) {
            item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
        }

        for (Map.Entry<Integer, ItemStack> entry : fixedItems.entrySet()) {
            if (SimilarItem.isSimilar(item, entry.getValue())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        for (Map.Entry<Integer, ItemStack> entry : fixedItems.entrySet()) {
            if (SimilarItem.isSimilar(item, entry.getValue())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Map.Entry<Integer, ItemStack> entry : fixedItems.entrySet()) {
            placeItem(event.getPlayer(), entry.getKey(), entry.getValue());
        }
    }


    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        for (Map.Entry<ItemStack, List<String>> entry : itemCommands.entrySet()) {
            if (SimilarItem.isSimilar(event.getItem(), entry.getKey())) {
                event.setCancelled(true);
                for (String command : entry.getValue()) {
                    if (command.contains("[console]")) {
                        command = command.replace("[console]", "");
                        CommandExecutor.runCommand(command, event.getPlayer(), true);
                    } else {
                        CommandExecutor.runCommand(command, event.getPlayer(), false);
                    }
                }
            }
        }
    }

    private void placeItem(Player player, int slot, ItemStack customItem) {
        if (player.getInventory().getItem(slot) == null || player.getInventory().getItem(slot).getType() == Material.AIR) {
            player.getInventory().setItem(slot, customItem);
            return;
        }
        if (SimilarItem.isSimilar(player.getInventory().getItem(slot), customItem)) {
            return;
        }
        if (player.getInventory().getItem(slot) != null || player.getInventory().getItem(slot).getType() != Material.AIR){
            sendItemToFreeSlot(slot, player);
        }
        player.getInventory().setItem(slot, customItem);
    }



    private void sendItemToFreeSlot(int from_slot, Player player) {
        int slot = getFreeSlot(player);
        if (slot == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getItem(from_slot));
            player.getInventory().setItem(from_slot, null);
            return;
        }
        player.getInventory().setItem(slot, player.getInventory().getItem(from_slot));
    }

    private int getFreeSlot(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType().isAir()) {
                return i;
            }
        }
        return -1;
    }
}
