package me.dawey.addons.inventory;

import me.dawey.addons.Addons;
import me.dawey.addons.chat.ChatColorManager;
import me.dawey.addons.utils.DataFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Stashes implements CommandExecutor, Listener, TabCompleter {

    private DataFile stashData;
    private Addons plugin;
    private List<Player> opened;
    private List<Player> editing;

    public Stashes(Addons plugin) {
        this.plugin = plugin;
        opened = new ArrayList<>();
        editing = new ArrayList<>();
        stashData = new DataFile(plugin, "data/stashes.yml");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (p.hasPermission("addons.stash.admin")) {
                        createStash(p);
                    } else {
                        p.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-permission")));
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (p.hasPermission("addons.stash.admin")) {
                        listStashes(p);
                    } else {
                        p.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-permission")));
                    }
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("open")) {
                    openStash(args[1], p, true);
                }

                if (args[0].equalsIgnoreCase("delete")) {
                    if (p.hasPermission("addons.stash.admin")) {
                        deleteStash(args[1], p);
                    } else {
                        p.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-permission")));
                    }
                }

                if (args[0].equalsIgnoreCase("create")) {
                    if (p.hasPermission("addons.stash.admin")) {
                        createStash(args[1], p);
                    } else {
                        p.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-permission")));
                    }
                }

                if (args[0].equalsIgnoreCase("edit")) {
                    if (p.hasPermission("addons.stash.admin")) {
                        editStash(args[1], p);
                    } else {
                        p.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-permission")));
                    }
                }
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("open") && args[2].equalsIgnoreCase("no-delete")) {
                    if (p.hasPermission("addons.stash.admin")) {
                        openStash(args[1], p, false);
                    } else {
                        p.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-permission")));
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (opened.contains(event.getPlayer())) {
            opened.remove(event.getPlayer());
            Inventory stash = event.getInventory();
            String converted = invToId(stash);
            if (!stashData.contains(event.getView().getTitle())) {
                stashData.set(event.getView().getTitle(), converted);
                stashData.save();
                event.getPlayer().sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("stash-created").replace("{stash-id}", event.getView().getTitle())));
            } else {
                event.getPlayer().sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("stash-exists")));
            }
        }

        if (editing.contains(event.getPlayer())) {
            editing.remove(event.getPlayer());
            Inventory stash = event.getInventory();
            String converted = invToId(stash);
            if (stashData.contains(event.getView().getTitle())) {
                stashData.set(event.getView().getTitle(), converted);
                stashData.save();
                event.getPlayer().sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("stash-edited").replace("{stash-id}", event.getView().getTitle())));
            } else {
                event.getPlayer().sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-stash")));
            }
        }
    }




    private void openStash(String id, Player player, boolean Delete) {
        if (stashData.contains(id)) {
            String stash = stashData.getString(id);
            Inventory inv = idToInv(stash, id);
            player.openInventory(inv);
            if (Delete) {
                stashData.set(id, null);
                stashData.save();
                player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("stash-opened").replace("{stash-id}", id)));
            }
        } else {
            player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-stash")));
        }
    }

    private void createStash(String id, Player player) {
        if (stashData.contains(id)) {
            player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("stash-exists")));
        }  else {
            Inventory stash = Bukkit.createInventory(null, 54, id);
            player.openInventory(stash);
            opened.add(player);
        }
    }

    private void listStashes(Player player) {
        player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stash-list-header")));
        for (String stash : stashData.getKeys(false)) {
            player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stash-list-item").replace("{stash-id}", stash)));
        }
    }

    private void createStash(Player player) {
        String id;
        do {
            id = getRandomId();
        } while (stashData.contains(id));
        Inventory stash = Bukkit.createInventory(null, 54, id);
        player.openInventory(stash);
        opened.add(player);
    }

    private void editStash(String id, Player player) {
        if (stashData.contains(id)) {
            String stash = stashData.getString(id);
            Inventory inv = idToInv(stash, id);
            player.openInventory(inv);
            editing.add(player);
        } else {
            player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-stash")));
        }
    }

    private void deleteStash(String id, Player player) {
        if (stashData.contains(id)) {
            stashData.set(id, null);
            stashData.save();
            player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("stash-removed").replace("{stash-id}", id)));
        } else {
            player.sendMessage(ChatColorManager.colorize(plugin.getMainConfig().getString("stashes-prefix") + plugin.getMainConfig().getString("no-stash")));
        }
    }


    List<String> commands = Arrays.asList("create", "open", "delete", "list", "edit");
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("addons.stash.admin")) {
            if (args.length == 1) {
                return commands;
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("edit")) {
                    return new ArrayList<>(stashData.getKeys(false));
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("open")) {
                    return Arrays.asList("no-delete");
                }
            }
        } else {
            if (args.length == 1) {
                return Arrays.asList("open");
            }
        }
        return null;
    }

    private static Inventory idToInv(String string, String title) {
        try {
            byte[] inventoryBytes = Base64.getDecoder().decode(string);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inventoryBytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Read the inventory size and contents
            int size = dataInput.readInt();
            ItemStack[] contents = (ItemStack[]) dataInput.readObject();

            // Create a new inventory of the appropriate type and size
            Inventory inventory = Bukkit.createInventory(null, size, title);

            // Set the inventory contents
            inventory.setContents(contents);

            dataInput.close();
            return inventory;
        } catch (IOException | ClassNotFoundException e) {
            Bukkit.getLogger().severe("Unable to deserialize inventory: " + e.getMessage());
            return null;
        }
    }

    private static String invToId(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            dataOutput.writeObject(inventory.getContents());
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Unable to serialize inventory: " + e.getMessage());
            return null;
        }
    }

    private static String getRandomId() {
        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}

