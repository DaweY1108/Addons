package me.dawey.addons.discord.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dawey.addons.Addons;
import me.dawey.addons.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.beryx.awt.color.ColorFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Stats extends ListenerAdapter {

    private final Addons plugin;

    public Stats(Addons plugin) {
        this.plugin = plugin;
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("stats")) {
            return;
        }

        Long userId = event.getUser().getIdLong();
        if (!plugin.getDatabase().isUserConnected(userId)) {
            event.reply("Nem sikerült lekérni a statisztikád, mivel a discordod nincs összekapcsolva a szerverrel!").queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(
                ColorFactory.valueOf(plugin.getDiscordConfig().getString("embed-color"))
        );
        String playerName = plugin.getDatabase().getPlayerName(userId);
        Player player = Bukkit.getPlayer(playerName);
        eb.setTitle(PlaceholderAPI.setPlaceholders(
                player == null ? Bukkit.getOfflinePlayer(playerName) : player,
                plugin.getDiscordConfig().getString("statistic.title"))
        );

        for (String line : plugin.getDiscordConfig().getStringList("statistic.text")) {
            eb.addField(
                    PlaceholderAPI.setPlaceholders(
                            player == null ? Bukkit.getOfflinePlayer(playerName) : player,
                            line.split(":")[0]
                    ),
                    PlaceholderAPI.setPlaceholders(
                            player == null ? Bukkit.getOfflinePlayer(playerName) : player,
                            line.split(":")[1]
                    ),
                    true
            );
        }
        event.reply("").setEmbeds(eb.build()).queue();
    }

    /*@Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("addsocial", "Felhaszáló összekötése a discorddal"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }*/

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("addsocial", "Felhaszáló összekötése a discorddal"));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
