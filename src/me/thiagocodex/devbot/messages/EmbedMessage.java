package me.thiagocodex.devbot.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;


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


    public MessageEmbed defaultRoleSuccess(String s) {
        return getInstance()
                .setColor(new Color(0x00FF00))
                .setDescription(s)
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


    public void sendPrivateCannotSendMessage(Member bot) {

        bot.getGuild().getMemberById("852974444954910730").getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Não consigo enviar mensagens no servidor " + bot.getGuild().getName() + " preciso da permissão: " + Permission.MESSAGE_WRITE.getName()).queue());


    }

}