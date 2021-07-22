package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.database.CRUD;
import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class Prefix extends ListenerAdapter {



    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = event.getChannel();

        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "prefix")) {

            textChannel.sendMessage("```fix\nO prefíxo para este servidor é: " + DevBot.prefixMap.get(event.getGuild().getId()) + "```").queue();

        }

        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "setprefix")) {

            if (!event.getMember().hasPermission(event.getChannel(), Permission.MESSAGE_ATTACH_FILES)) {
                textChannel.sendMessage("Você não tem permissão para fazer isso.").queue();
                return;
            }


            DevBot.prefixMap.replace(event.getGuild().getId(), args[1].charAt(0));
            try {
                CRUD.update("prefix", event.getGuild().getId(), String.valueOf(args[1].charAt(0)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            textChannel.sendMessage("O prefíxo foi alterado para: " + DevBot.prefixMap.get(event.getGuild().getId())).queue();

        }
    }
}
