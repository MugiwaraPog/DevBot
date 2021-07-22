package me.thiagocodex.devbot.events;

import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        TextChannel textChannel = event.getGuild().getTextChannelById(855060202667507764L);
        String userName = event.getUser().getName();
        Member joined = event.getMember();
        Guild guild = event.getGuild();
        textChannel.sendMessage(userName + " entrou no servidor!").queue();
        guild.addRoleToMember(joined.getId(), guild.getRoleById(DevBot.autoroleMap.get(guild.getId()))).queue();

    }
}
