package me.dawey.addons.luckperms;

import me.dawey.addons.Addons;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;

public class GroupChangeListener {
    private final Addons plugin;
    public GroupChangeListener(LuckPerms provider, Addons plugin) {
        this.plugin = plugin;
        EventBus eventBus = provider.getEventBus();
        eventBus.subscribe(
                this.plugin,
                UserDataRecalculateEvent.class,
                e -> {
                    Addons.getDiscord().sendSystemMessage("User " + e.getUser().getUsername() + " group changed to " + e.getUser().getPrimaryGroup() + "!");
                }
        );
    }

}
