package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.requests.WebSocketClient;
import org.jetbrains.annotations.NotNull;

public class Ping extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = event.getChannel();

        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "ping")) {
            textChannel.sendMessage(DevBot.jda.getGatewayPing() + "ms").queue();
        }
    }
}
