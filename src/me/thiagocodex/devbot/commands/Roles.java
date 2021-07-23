package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.database.CRUD;
import me.thiagocodex.devbot.main.DevBot;
import me.thiagocodex.devbot.messages.EmbedMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Roles extends ListenerAdapter {

    private final Map<Byte, String> rolesMap = new HashMap<>();
    private final Map<String, Map<Byte, String>> guildRolesMapMap = new HashMap<>();
    private final Map<String, Boolean> isEditingAutoroleMap = new HashMap<>();
    private final Map<String, Member> memberEditingAutoroleMap = new HashMap<>();
    private final EmbedMessage embedMessage = new EmbedMessage();


    public void sendIfPermitted(User admin, TextChannel textChannel, MessageEmbed messageEmbed) {

        if (textChannel.getGuild().getSelfMember().hasPermission(textChannel, Permission.MESSAGE_WRITE)) {
            textChannel.sendMessage(messageEmbed).queue();
        } else {
            embedMessage.sendPrivateCannotSendMessage(admin, textChannel.getGuild().getSelfMember());
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        byte roleIndexes = 0;

        String gId = event.getGuild().getId();

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(gId) + "autorole")) {


            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                sendIfPermitted(event.getMember().getUser(), event.getChannel(), embedMessage.noPerm(event.getMember()));
                return;
            }


            StringBuilder stringBuilder = new StringBuilder();
            for (Role role : event.getGuild().getRoles()) {
                if (!role.isPublicRole() && role.getPosition() < event.getGuild().getBotRole().getPosition()) {

                    rolesMap.put(roleIndexes, role.getId());

                    stringBuilder
                            .append("`")
                            .append(roleIndexes)
                            .append("`")
                            .append(" - ")
                            .append(role.getName())
                            .append("\n");
                    roleIndexes++;
                }
            }

            if (stringBuilder.length() == 0) {
                sendIfPermitted(event.getMember().getUser(), event.getChannel(), embedMessage.noLowerRoles());
                return;
            }


            guildRolesMapMap.put(gId, rolesMap);
            isEditingAutoroleMap.put(gId, true);
            memberEditingAutoroleMap.put(gId, event.getMember());

            sendIfPermitted(event.getMember().getUser(), event.getChannel(), embedMessage.listRolesIndexes(stringBuilder));

        } else if (args[0].matches("^[0-9]{1,3}$")) {

            if (isEditingAutoroleMap.get(gId) == null ||
                    !isEditingAutoroleMap.get(gId) ||
                    !event.getMember().equals(memberEditingAutoroleMap.get(event.getGuild().getId())) ||
                    event.getJDA().getSelfUser().getId().equals(event.getMember().getId()) ||
                    guildRolesMapMap.get(event.getGuild().getId()) == null
            ) return;


            if (Byte.parseByte(args[0]) >= guildRolesMapMap.get(gId).size()) {
                sendIfPermitted(event.getMember().getUser(), event.getChannel(), embedMessage.invalidIndex());
                return;
            }

            sendIfPermitted(event.getMember().getUser(), event.getChannel(),
                    embedMessage.defaultRoleSuccess(event.getGuild().getRoleById(guildRolesMapMap.get(gId).get(Byte.parseByte(args[0]))).getName()));

            try {
                CRUD.update("autorole", gId, guildRolesMapMap.get(gId).get(Byte.parseByte(args[0])));
                DevBot.autoroleMap.replace(gId, guildRolesMapMap.get(gId).get(Byte.parseByte(args[0])));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            isEditingAutoroleMap.replace(event.getGuild().getId(), false);
        }
    }
}

