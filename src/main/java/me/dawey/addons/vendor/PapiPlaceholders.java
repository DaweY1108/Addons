package me.dawey.addons.vendor;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dawey.addons.Addons;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiPlaceholders extends PlaceholderExpansion {
    private Addons plugin;

    public PapiPlaceholders(Addons plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public String getIdentifier() {
        return "addons";
    }

    @NotNull
    public String getAuthor() {
        return "dawey";
    }

    @NotNull
    public String getVersion() {
        return "2.0";
    }

    public boolean canRegister() {
        return true;
    }

    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player p, @NotNull String identifier) {
        if (p == null)
            return "";
        for (String placeholder : this.plugin.getMainConfig().getConfigurationSection("papi_placeholders").getKeys(false)) {
            if (identifier.equalsIgnoreCase(placeholder))
                return this.plugin.getMainConfig().getString("papi_placeholders." + placeholder);
        }
        if (identifier.equalsIgnoreCase("is_online")) {
            return p.isOnline() ? "Igen" : "Nem";

        }
        return null;
    }
}
