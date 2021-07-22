package me.thiagocodex.devbot.events;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberLeave extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        TextChannel textChannel = event.getGuild().getTextChannelById(855063197324935229L);
        String userName = event.getUser().getName();
        textChannel.sendMessage(userName + " saiu do servidor!").queue();
    }
}
