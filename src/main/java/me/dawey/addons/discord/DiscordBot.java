package me.dawey.addons.discord;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import me.dawey.addons.discord.commands.Stats;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscordBot {

    private String token;
    private JDA bot;
    private Addons plugin;
    private String guildId;
    private Map<String, String> roles;
    public DiscordBot(Addons plugin) {
        this.plugin = plugin;
        this.token = plugin.getDiscordConfig().getString("bot.token");
        this.guildId = plugin.getDiscordConfig().getString("bot.guild-id");
        this.roles = new HashMap<>();
        for (String name : plugin.getDiscordConfig().getConfigurationSection("roles").getKeys(false)) {
            roles.put(name, plugin.getDiscordConfig().getString("roles." + name));
        }
    }

    public void start() {
        try {
            bot = JDABuilder.createDefault(token)
                    .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build()
                    .awaitReady();
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        bot.shutdown();
    }

    public void restart() {
        stop();
        start();
    }

    public void init() {
        descriptions();
        commands();
    }

    public void commands() {
        bot.addEventListener(new Stats(plugin));
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("stats", "Szerver statisztikád megtekintése"));
        bot.updateCommands().addCommands(commandData).queue();
    }

    public void descriptions() {
        AtomicInteger index = new AtomicInteger(0);
        List<String> descriptions = plugin.getDiscordConfig().getStringList("descriptions");

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if (index.get() >= descriptions.size()) {
                index.set(0); // Reset index if it's out of bounds
            }
            String currentDescription = descriptions.get(index.getAndIncrement());
            currentDescription = PlaceholderAPI.setPlaceholders(null, currentDescription);
            bot.getPresence().setActivity(Activity.playing(currentDescription));
        }, 0L, 20L * 30L);
    }

    public void setRole(String userName, String roleName) {
        Bukkit.getScheduler().runTaskAsynchronously(Addons.getInstance(), () -> {
            if(!roles.containsKey(roleName)) {
                return;
            }
            User user = bot.getUserById(userName);
            if (user == null) {
                return;
            }
            for (String name : roles.keySet()) {
                bot.getGuildById(guildId).removeRoleFromMember(bot.getGuildById(guildId).getMember(user), bot.getRoleById(roles.get(name))).queue();
            }
            bot.getGuildById(guildId).addRoleToMember(bot.getGuildById(guildId).getMember(user), bot.getRoleById(roles.get(roleName))).queue();
        });

    }

    public void sendSystemMessage(String message) {
        Bukkit.getScheduler().runTaskAsynchronously(Addons.getInstance(), () -> {
            TextChannel channel = bot.getTextChannelById(plugin.getDiscordConfig().getString("channels.server-channel"));
            channel.sendMessage(message).queue();
        });
    }

    public List<String> getUsers() {
        List<String> users = new ArrayList<>();
        for (Member member : bot.getGuildById(guildId).getMembers()) {
            users.add(member.getUser().getAsTag());
        }
        return users;
    }
}
