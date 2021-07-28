package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BulkDelete extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "clear")) {
            event.getChannel().deleteMessages(event.getChannel().getHistory().retrievePast(30).complete()).queue();

        }


    }
}
