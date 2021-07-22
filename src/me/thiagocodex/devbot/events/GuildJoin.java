package me.thiagocodex.devbot.events;

import me.thiagocodex.devbot.database.CRUD;
import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class GuildJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        try {
            CRUD.insert(event.getGuild().getId(), '$');
            CRUD.select(event.getGuild().getId());

            DevBot.setActivity(event.getJDA());

        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }
    }
}
