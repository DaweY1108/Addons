package me.dawey.addons.inventory;

import me.dawey.addons.Addons;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InventoryCheck implements CommandExecutor, Listener {
    private Addons plugin;


    private static Map<Player, String> inventoryCheckers = new HashMap<>();

    public InventoryCheck(Addons plugin) {
        this.plugin = plugin;
    }

    public static void openInventory(Player inventoryHolder, Player player) {
        Inventory inv = Bukkit.createInventory(inventoryHolder, 36, "§c" + inventoryHolder.getName() + " §0Leltárja");
        inv.setContents(inventoryHolder.getInventory().getContents());
        inventoryCheckers.put(player, "§c" + inventoryHolder.getName() + " §0Leltárja");
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent e) {
        if (inventoryCheckers.containsKey(e.getPlayer())) {
            inventoryCheckers.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (inventoryCheckers.get(e.getWhoClicked()) != null) {
                if (e.getView().getTitle().equalsIgnoreCase(inventoryCheckers.get(e.getWhoClicked()))) {
                    e.setCancelled(true);
                }
            }
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase(plugin.getMainConfig().getString("inv-command-code"))) {
                    Player p = (Player) sender;
                    Player target = p.getServer().getPlayer(args[1]);
                    if (target != null) {
                        openInventory(target, p);
                    }
                }
            }
            return false;
        }
        return false;
    }
}
