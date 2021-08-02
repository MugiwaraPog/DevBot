package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BulkDelete extends ListenerAdapter {

    private final Map<String, Boolean> isDeletingCreatingChannel = new HashMap<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "clear")) {
            event.getChannel().deleteMessages(event.getChannel().getHistory().retrievePast(30).complete()).queue();

        }

        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "clearall")) {

            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.getChannel().sendMessage("você não tem permissão para limpar este canal.").queue();
                return;
            }

            isDeletingCreatingChannel.put(event.getGuild().getId(), true);

        }

        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "confirm")) {
            if (isDeletingCreatingChannel.get(event.getGuild().getId())) {

                event.getChannel().createCopy().queue();
                event.getChannel().delete().queue();

                isDeletingCreatingChannel.replace(event.getGuild().getId(), false);

            }
        }
    }
}
