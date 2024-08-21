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

public class CustomItem extends ItemStack {

        private ConfigurationSection configSection;

        public CustomItem(ConfigurationSection section) {
            this.configSection = section;
            createItem(1);
        }
        public CustomItem(ConfigurationSection section, int amount) {
            this.configSection = section;
            createItem(amount);
        }

        public ConfigurationSection saveToSection(String sectionName, ConfigurationSection parentSection) {
            ConfigurationSection section = parentSection.createSection(sectionName);
            section.set("id", this.getType().toString().toLowerCase());
            section.set("amount", this.getAmount());
            if (this.getItemMeta().hasDisplayName()) section.set("name", this.getItemMeta().getDisplayName());

            return section;
        }

        public List<String> customLore;

        private void createItem(int amount) {
            if (configSection == null) {
                this.setType(Material.AIR);
                this.setAmount(1);
                return;
            }

            if (Material.getMaterial(configSection.getString("id")) == null) {
                Logger.getLogger().warn("Nincs ilyen material: " + configSection.getString("id"));
            }

            //Item ID-k kezelése
            this.setType(Material.getMaterial(configSection.getString("id")));
            this.setAmount(amount);

            if (configSection.contains("mobType") && this.getType() == Material.SPAWNER) {
                BlockStateMeta bsm = (BlockStateMeta) this.getItemMeta();
                CreatureSpawner cs = (CreatureSpawner) bsm.getBlockState();
                cs.setSpawnedType(EntityType.valueOf(configSection.getString("mobType")));
                cs.setSpawnCount(0);
                bsm.setBlockState(cs);
                this.setItemMeta(bsm);
            }

            //Meta inicalizálása
            ItemMeta meta = this.getItemMeta();
            boolean modifiedMeta = false;

            //Item mennyiseg kezelese
            if (configSection.contains("amount")) {
                this.setAmount(configSection.getInt("amount"));
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
                customLore = new ArrayList<>();
                for (String s : dummy) {
                    lore.add(ChatColorManager.colorize(s));
                    customLore.add(ChatColorManager.colorize(s));
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
                        case "arrowdamage":
                            meta.addEnchant(Enchantment.ARROW_DAMAGE, enchantmentLevel, true);
                            break;
                        case "arrowfire":
                            meta.addEnchant(Enchantment.ARROW_FIRE, enchantmentLevel, true);
                            break;
                        case "arrowinfinite":
                            meta.addEnchant(Enchantment.ARROW_INFINITE, enchantmentLevel, true);
                            break;
                        case "arrowknockback":
                            meta.addEnchant(Enchantment.ARROW_KNOCKBACK, enchantmentLevel, true);
                            break;
                        case "damage":
                            meta.addEnchant(Enchantment.DAMAGE_ALL, enchantmentLevel, true);
                            break;
                        case "digspeed":
                            meta.addEnchant(Enchantment.DIG_SPEED, enchantmentLevel, true);
                            break;
                        case "durability":
                            meta.addEnchant(Enchantment.DURABILITY, enchantmentLevel, true);
                            break;
                        case "fireaspect":
                            meta.addEnchant(Enchantment.FIRE_ASPECT, enchantmentLevel, true);
                            break;
                        case "knockback":
                            meta.addEnchant(Enchantment.KNOCKBACK, enchantmentLevel, true);
                            break;
                        case "lootbonusblock":
                            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, enchantmentLevel, true);
                            break;
                        case "lootbonusmob":
                            meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, enchantmentLevel, true);
                            break;
                        case "luck":
                            meta.addEnchant(Enchantment.LUCK, enchantmentLevel, true);
                            break;
                        case "protectionfall":
                            meta.addEnchant(Enchantment.PROTECTION_FALL, enchantmentLevel, true);
                            break;
                        case "protectionfire":
                            meta.addEnchant(Enchantment.PROTECTION_FALL, enchantmentLevel, true);
                            break;
                        case "silktouch":
                            meta.addEnchant(Enchantment.SILK_TOUCH, enchantmentLevel, true);
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
                this.setItemMeta(meta);
            }
        }

    }
