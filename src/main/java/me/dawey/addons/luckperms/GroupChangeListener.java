package me.dawey.addons.luckperms;

import me.dawey.addons.Addons;
import me.dawey.addons.utils.Logger;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.track.UserTrackEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class GroupChangeListener {
    private final Addons plugin;
    public GroupChangeListener(LuckPerms provider, Addons plugin) {
        this.plugin = plugin;
        EventBus eventBus = provider.getEventBus();
        eventBus.subscribe(this.plugin, NodeAddEvent.class, this::onNodeAdd);
        //eventBus.subscribe(this.plugin, NodeRemoveEvent.class, this::onNodeRemove);
    }

    private void onNodeAdd(NodeAddEvent e) {
        if (!e.isUser()) {
            return;
        }
        User target = (User) e.getTarget();
        Node node = e.getNode();

        // LuckPerms events are posted async, we want to process on the server thread!
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            Player player = this.plugin.getServer().getPlayer(target.getUniqueId());
            if (player == null) {
                return; // Player not online.
            }

            if (node instanceof PermissionNode) {
                /*String permission = ((PermissionNode) node).getPermission();
                player.sendMessage(ChatColor.YELLOW + "You were given the " + permission + " permission!");*/

            } else if (node instanceof InheritanceNode) {


                String groupName = ((InheritanceNode) node).getGroupName();
                String discordId = this.plugin.getDatabase().getDiscordId(player.getName().toLowerCase());
                if (discordId != null && !discordId.equalsIgnoreCase(""))
                    this.plugin.getDiscordBot().setRole(discordId, groupName);


            } else if (node instanceof PrefixNode) {
                /*String prefix = ((PrefixNode) node).getMetaValue();
                player.sendMessage(ChatColor.YELLOW + "You were given the " + prefix + " prefix!");*/

            } else if (node instanceof SuffixNode) {
                /*String suffix = ((SuffixNode) node).getMetaValue();
                player.sendMessage(ChatColor.YELLOW + "You were given the " + suffix + " suffix!");*/
            }
        });
    }

    /*private void onNodeRemove(NodeRemoveEvent e) {
        if (!e.isUser()) {
            return;
        }

        User target = (User) e.getTarget();
        Node node = e.getNode();

        // LuckPerms events are posted async, we want to process on the server thread!
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            Player player = this.plugin.getServer().getPlayer(target.getUniqueId());
            if (player == null) {
                return; // Player not online.
            }

            if (node instanceof PermissionNode) {
                String permission = ((PermissionNode) node).getPermission();
                player.sendMessage(ChatColor.DARK_RED + "You no longer have the " + permission + " permission!");

            } else if (node instanceof InheritanceNode) {
                String groupName = ((InheritanceNode) node).getGroupName();
                player.sendMessage(ChatColor.DARK_RED + "You are no longer in the " + groupName + " group!");

            } else if (node instanceof PrefixNode) {
                String prefix = ((PrefixNode) node).getMetaValue();
                player.sendMessage(ChatColor.DARK_RED + "You no longer have the " + prefix + " prefix!");

            } else if (node instanceof SuffixNode) {
                String suffix = ((SuffixNode) node).getMetaValue();
                player.sendMessage(ChatColor.DARK_RED + "You no longer have the " + suffix + " suffix!");
            }
        });
    }*/

}
