package me.thiagocodex.devbot.events;

import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeave extends ListenerAdapter {

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {

        try {
            DevBot.setActivity(event.getJDA());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
