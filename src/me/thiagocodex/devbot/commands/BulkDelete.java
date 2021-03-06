package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.main.DevBot;
import me.thiagocodex.devbot.messages.EmbedMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BulkDelete extends ListenerAdapter {

    private final Map<String, Boolean> isDeletingCreatingChannel = new HashMap<>();
    private final EmbedMessage embedMessage = new EmbedMessage();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "clear")) {

            User author = event.getAuthor();
            TextChannel textChannel = event.getChannel();
            String authorName = author.getName();

            if (args[1].matches("^[\\p{Digit}]{1,3}$")) {
                int num = Integer.parseInt(args[1]);
                if (num <= 100 && num > 1) {
                    num = num == 100 ? 99 : num;
                } else if (num == 1) {
                    DevBot.sendIfPermitted(author, textChannel, DevBot.embedMessage.info(
                            authorName + ", sério? Você não precisa deletar em massa 1 mensagem. Exclua-a."));
                    return;
                } else {
                    DevBot.sendIfPermitted(author, textChannel, DevBot.embedMessage.info(
                            authorName + ", você não pode apagar mais de 100 mensagens de uma só vez."));
                    return;
                }

                event.getChannel().deleteMessages(event.getChannel().getHistory().retrievePast(num + 1)
                        .complete().stream().filter(message -> !message.isPinned()).collect(Collectors.toList())).queue();


/*                try {

                } catch (IllegalArgumentException exception) {
                    //event.getChannel().sendMessage(exception.getMessage()).queue();
                    event.getChannel().sendMessage(embedMessage.error(event.getAuthor().getName()
                            + ", não pode apagar mensagens mais velhas que 2 semanas.")).queue();
                }*/
            }
        }

        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "clearall")) {
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.getChannel().sendMessage("Você não tem permissão para limpar este canal.").queue();
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
