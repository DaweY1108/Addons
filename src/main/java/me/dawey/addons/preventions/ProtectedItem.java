package me.dawey.addons.preventions;

import me.dawey.addons.Addons;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ProtectedItem extends ItemStack {
    private Addons plugin;

    public ProtectedItem(ItemStack itemStack, Addons plugin) {
        super(itemStack);
        this.plugin = plugin;
    }

    public boolean isProtected()
    {
        List<String> identifiers = plugin.getMainConfig().getStringList("protected-identifiers");
        if (this.getItemMeta().getLore() == null) {
            return false;
        }
        List<String> lore = this.getItemMeta().getLore();
        for (String identifier : identifiers) {
            for (String line : lore) {
                if (line.contains(identifier)) {
                    return true;
                }
            }
        }
        return false;
    }
}
