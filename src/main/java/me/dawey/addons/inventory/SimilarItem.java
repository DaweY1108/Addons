package me.dawey.addons.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SimilarItem {
    public static boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (item2.getType() != item1.getType())
            return false;
        if (item2.hasItemMeta() != item1.hasItemMeta())
            return false;
        if (item2.hasItemMeta()) {
            ItemMeta item1Meta = item1.getItemMeta();
            ItemMeta item2Meta = item2.getItemMeta();
            if (item2Meta.hasDisplayName() != item1Meta.hasDisplayName())
                return false;
            if (item2Meta.hasDisplayName() &&
                    !item2Meta.getDisplayName().equals(item1Meta.getDisplayName()))
                return false;
            if (item2Meta.hasLore() != item1Meta.hasLore())
                return false;
            if (item2Meta instanceof Damageable != item1Meta instanceof Damageable)
                return false;
            if (item2Meta instanceof Damageable) {
                Damageable dam1 = (Damageable)item1Meta;
                Damageable dam2 = (Damageable)item2Meta;
                if (dam1.hasDamage() != dam2.hasDamage())
                    return false;
                if (dam2.hasDamage() &&
                        dam2.getDamage() != dam1.getDamage())
                    return false;
            }
        }
        return true;
    }
}
