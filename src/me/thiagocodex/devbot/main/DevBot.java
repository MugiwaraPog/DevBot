package me.thiagocodex.devbot.main;

import me.thiagocodex.devbot.commands.*;
import me.thiagocodex.devbot.database.CRUD;
import me.thiagocodex.devbot.database.Config;
import me.thiagocodex.devbot.events.GuildJoin;
import me.thiagocodex.devbot.events.GuildLeave;
import me.thiagocodex.devbot.events.MemberJoin;
import me.thiagocodex.devbot.events.MemberLeave;
import me.thiagocodex.devbot.messages.EmbedMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;


import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class DevBot {

    public static JDA jda;

    public static Map<String, Character> prefixMap = new HashMap<>();
    public static Map<String, String> autoroleMap = new HashMap<>();
    public static final EmbedMessage embedMessage = new EmbedMessage();

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, IOException {

        Config.createFilesAndTable();

        jda = JDABuilder.create(System.getenv("BOT_TOKEN"),
                EnumSet.allOf(GatewayIntent.class)).build();

        jda.addEventListener(
                new Ping(),
                new MemberJoin(),
                new MemberLeave(),
                new Prefix(),
                new Roles(),
                new GuildJoin(),
                new GuildLeave(),
                new BulkDelete(),
                new PrivateBulkDelete());

        for (Guild guild : jda.awaitReady().getGuilds()) {
            CRUD.insert(guild.getId(), '$');
        }

        for (Guild guild : jda.awaitReady().getGuilds()) {
            CRUD.select(guild.getId());
        }

        setActivity(jda);
    }


    public static void setActivity(JDA jda) throws InterruptedException {
        jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.playing("em " + jda.awaitReady().getGuilds().size() + " servidores"));

    }

    public static void sendIfPermitted(User admin, TextChannel textChannel, MessageEmbed messageEmbed) {

        if (textChannel.getGuild().getSelfMember().hasPermission(textChannel, Permission.MESSAGE_WRITE)) {
            textChannel.sendMessage(messageEmbed).queue();
        } else {
            embedMessage.sendPrivateCannotSendMessage(admin, textChannel.getGuild().getSelfMember());
        }
    }

}
