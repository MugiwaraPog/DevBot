package me.thiagocodex.devbot.commands;

import me.thiagocodex.devbot.main.DevBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BulkDelete extends ListenerAdapter {

    private final Map<String, Boolean> isDeletingCreatingChannel = new HashMap<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(DevBot.prefixMap.get(event.getGuild().getId()) + "clear")) {

            int num = Integer.parseInt(args[1]);

            if (num <= 100 && num > 1) {
                num = num >= 100 ? 99 : num;
            } else if (num == 1) {
                event.getChannel().sendMessage("Sério? Você não precisa deletar em massa 1 mensagem. Exclua-a").queue();
                return;
            } else {
                event.getChannel().sendMessage("Você não pode apagar mais de 100 mensagens de uma só vez.").queue();
                return;
            }
            event.getChannel().deleteMessages(event.getChannel().getHistory().retrievePast(num + 1)
                    .complete().stream().filter(message -> !message.isPinned()).collect(Collectors.toList())).queue();
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
