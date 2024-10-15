package me.dawey.addons.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.dawey.addons.chat.ChatColorManager;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomItem {

        public static ItemStack createItem(ConfigurationSection configSection, int amount) {
            ItemStack item = new ItemStack(Material.AIR);

            if (Material.getMaterial(configSection.getString("id")) == null) {
                Logger.getLogger().warn("Nincs ilyen material: " + configSection.getString("id"));
                return item;
            }
            item = new ItemStack(Material.getMaterial(configSection.getString("id")));

            //Item ID-k kezelése
            item.setAmount(amount);

            //Meta inicalizálása
            ItemMeta meta = item.getItemMeta();
            boolean modifiedMeta = false;

            //Item mennyiseg kezelese
            if (configSection.contains("amount")) {
                item.setAmount(configSection.getInt("amount"));
            }

            //Item nevenek kezelese
            if (configSection.contains("name")) {
                meta.setDisplayName(ChatColorManager.colorize(configSection.getString("name")));
                modifiedMeta = true;
            }

            //Item leirasanak kezelese
            if (configSection.contains("lore")) {
                List<String> dummy = configSection.getStringList("lore");
                List<String> lore = new ArrayList<String>();
                for (String s : dummy) {
                    lore.add(ChatColorManager.colorize(s));
                }
                meta.setLore(lore);
                modifiedMeta = true;
            }

            if (configSection.getString("id").equalsIgnoreCase("spawner")) {
                meta.setCustomModelData(1);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }

            //Item enchantok kezelese
            if (configSection.contains("enchantments")) {
                List<String> enchantmentStrings = configSection.getStringList("enchantments");
                for (String enchantmentString : enchantmentStrings) {
                    String enchantmentName = enchantmentString.split(":")[0];
                    int enchantmentLevel = Integer.valueOf(enchantmentString.split(":")[1]);
                    switch (enchantmentName) {
                        case "AQUA_AFFINITY":
                            meta.addEnchant(Enchantment.AQUA_AFFINITY, enchantmentLevel, true);
                            break;
                        case "BANE_OF_ARTHROPODS":
                            meta.addEnchant(Enchantment.BANE_OF_ARTHROPODS, enchantmentLevel, true);
                            break;
                        case "BINDING_CURSE":
                            meta.addEnchant(Enchantment.BINDING_CURSE, enchantmentLevel, true);
                            break;
                        case "BLAST_PROTECTION":
                            meta.addEnchant(Enchantment.BLAST_PROTECTION, enchantmentLevel, true);
                            break;
                        case "BREACH":
                            meta.addEnchant(Enchantment.BREACH, enchantmentLevel, true);
                            break;
                        case "CHANNELING":
                            meta.addEnchant(Enchantment.CHANNELING, enchantmentLevel, true);
                            break;
                        case "DENSITY":
                            meta.addEnchant(Enchantment.DENSITY, enchantmentLevel, true);
                            break;
                        case "DEPTH_STRIDER":
                            meta.addEnchant(Enchantment.DEPTH_STRIDER, enchantmentLevel, true);
                            break;
                        case "EFFICIENCY":
                            meta.addEnchant(Enchantment.EFFICIENCY, enchantmentLevel, true);
                            break;
                        case "FEATHER_FALLING":
                            meta.addEnchant(Enchantment.FEATHER_FALLING, enchantmentLevel, true);
                            break;
                        case "FIRE_ASPECT":
                            meta.addEnchant(Enchantment.FIRE_ASPECT, enchantmentLevel, true);
                            break;
                        case "FIRE_PROTECTION":
                            meta.addEnchant(Enchantment.FIRE_PROTECTION, enchantmentLevel, true);
                            break;
                        case "FLAME":
                            meta.addEnchant(Enchantment.FLAME, enchantmentLevel, true);
                            break;
                        case "FORTUNE":
                            meta.addEnchant(Enchantment.FORTUNE, enchantmentLevel, true);
                            break;
                        case "FROST_WALKER":
                            meta.addEnchant(Enchantment.FROST_WALKER, enchantmentLevel, true);
                            break;
                        case "IMPALING":
                            meta.addEnchant(Enchantment.IMPALING, enchantmentLevel, true);
                            break;
                        case "INFINITY":
                            meta.addEnchant(Enchantment.INFINITY, enchantmentLevel, true);
                            break;
                        case "KNOCKBACK":
                            meta.addEnchant(Enchantment.KNOCKBACK, enchantmentLevel, true);
                            break;
                        case "LOOTING":
                            meta.addEnchant(Enchantment.LOOTING, enchantmentLevel, true);
                            break;
                        case "LOYALTY":
                            meta.addEnchant(Enchantment.LOYALTY, enchantmentLevel, true);
                            break;
                        case "LUCK_OF_THE_SEA":
                            meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, enchantmentLevel, true);
                            break;
                        case "LURE":
                            meta.addEnchant(Enchantment.LURE, enchantmentLevel, true);
                            break;
                        case "MENDING":
                            meta.addEnchant(Enchantment.MENDING, enchantmentLevel, true);
                            break;
                        case "MULTISHOT":
                            meta.addEnchant(Enchantment.MULTISHOT, enchantmentLevel, true);
                            break;
                        case "PIERCING":
                            meta.addEnchant(Enchantment.PIERCING, enchantmentLevel, true);
                            break;
                        case "POWER":
                            meta.addEnchant(Enchantment.POWER, enchantmentLevel, true);
                            break;
                        case "PROJECTILE_PROTECTION":
                            meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, enchantmentLevel, true);
                            break;
                        case "PROTECTION":
                            meta.addEnchant(Enchantment.PROTECTION, enchantmentLevel, true);
                            break;
                        case "PUNCH":
                            meta.addEnchant(Enchantment.PUNCH, enchantmentLevel, true);
                            break;
                        case "QUICK_CHARGE":
                            meta.addEnchant(Enchantment.QUICK_CHARGE, enchantmentLevel, true);
                            break;
                        case "RESPIRATION":
                            meta.addEnchant(Enchantment.RESPIRATION, enchantmentLevel, true);
                            break;
                        case "RIPTIDE":
                            meta.addEnchant(Enchantment.RIPTIDE, enchantmentLevel, true);
                            break;
                        case "SHARPNESS":
                            meta.addEnchant(Enchantment.SHARPNESS, enchantmentLevel, true);
                            break;
                        case "SMITE":
                            meta.addEnchant(Enchantment.SMITE, enchantmentLevel, true);
                            break;
                        case "SWEEPING_EDGE":
                            meta.addEnchant(Enchantment.SWEEPING_EDGE, enchantmentLevel, true);
                            break;
                        case "THORNS":
                            meta.addEnchant(Enchantment.THORNS, enchantmentLevel, true);
                            break;
                        case "UNBREAKING":
                            meta.addEnchant(Enchantment.UNBREAKING, enchantmentLevel, true);
                            break;
                        case "VANISHING_CURSE":
                            meta.addEnchant(Enchantment.VANISHING_CURSE, enchantmentLevel, true);
                            break;
                        default:
                            System.out.println("Ismeretlen enchantment: " + enchantmentName);
                            break;
                    }
                }
                modifiedMeta = true;
            }

            //Item fejtextura kezelese
            if (configSection.contains("skullTextureID")) {
                String skullTextureID = configSection.getString("skullTextureID");
                String skullTextureUUID = "89b4e39d-7070-493f-b27a-8f4d06d27d3b";
                if (skullTextureID != null) {
                    String skinURL = "http://textures.minecraft.net/texture/" + skullTextureID;
                    SkullMeta skullMeta = (SkullMeta) meta;
                    skullMeta.setOwner("YouCantFindMe");
                    GameProfile profile = new GameProfile((UUID.fromString(skullTextureUUID)), null);
                    byte[] encodedData = Base64.encodeBase64(("{textures:{SKIN:{url:\"" + skinURL + "\"}}}").getBytes());
                    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
                    Field profileField = null;
                    try {
                        profileField = skullMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(skullMeta, profile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    meta = skullMeta;
                    modifiedMeta = true;
                }
            }

            //Item tagek kezelése
            if (configSection.contains("tags")) {
                for (String tags : configSection.getStringList("tags")) {
                    String[] tagList = tags.split(":");
                    String tagName = tagList[0];
                    String tagValue = tagList[1];
                    switch (tagName) {
                        case "vanilladurability":
                            Damageable dam = (Damageable) meta;
                            dam.setDamage(Integer.valueOf(tagValue));
                            meta = (ItemMeta) dam;
                            break;
                        case "unbreakable":
                            meta.setUnbreakable(Boolean.valueOf(tagValue));
                            break;
                        case "custommodeldata":
                            meta.setCustomModelData(Integer.valueOf(tagValue));
                            break;
                    }
                }
                modifiedMeta = true;
            }

            //Flagek kezelése
            if (configSection.contains("flags")) {
                for (String flags : configSection.getStringList("flags")) {
                    meta.addItemFlags(ItemFlag.valueOf(flags));
                }
                modifiedMeta = true;
            }

            //Itemmeta hozzáadása az itemhez
            if (modifiedMeta) {
                item.setItemMeta(meta);
            }
            return item;
        }

    }
