package me.thiagocodex.devbot.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.concurrent.TimeUnit;


public class EmbedMessage {

    public EmbedBuilder instance;

    public EmbedBuilder getInstance() {
        return instance == null ? new EmbedBuilder() : instance.clear();
    }

    public MessageEmbed noPerm(Member member) {
        return getInstance()
                .setColor(new Color(0xFF0000))
                .setDescription("`" + member.getUser().getName() + "` você não tem permissão para mudar o cargo padrão.")
                .build();
    }


    public MessageEmbed listRolesIndexes(StringBuilder stringBuilder) {
        return getInstance()
                .setColor(new Color(0x00FFFF))
                .setDescription(stringBuilder)
                .setTitle("Escolha o índice do cargo para ser o cargo padrão.")
                .build();
    }


    public MessageEmbed defaultRoleSuccess(String roleName) {
        return getInstance()
                .setColor(new Color(0x00FF00))
                .setDescription("Definido cargo padrão para novos membros como: " + roleName)
                .build();
    }


    public MessageEmbed noLowerRoles() {
        return getInstance()
                .setColor(new Color(0xFFFF00))
                .setDescription("Não existem cargo abaixo do cargo do DevBot, verifique o gerenciador de cargos.")
                .build();
    }


    public MessageEmbed invalidIndex() {
        return getInstance().setColor(new Color(0xFF0000))
                .setDescription("Índice inválido, digite o índice de um cargo da lista.")
                .build();
    }



    public void sendPrivateCannotSendMessage(User admin, Member bot) {

        admin.openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessage(getInstance().setColor(new Color(0xFF0000))
                        .setDescription("Não consigo enviar mensagens no servidor " + bot.getGuild().getName() + " preciso da permissão: " + Permission.MESSAGE_WRITE.getName())
                        .build()).queue(message -> message.delete().queueAfter(1, TimeUnit.MINUTES)));

    }

}
