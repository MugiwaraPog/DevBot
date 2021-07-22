package me.thiagocodex.devbot.database;

import me.thiagocodex.devbot.main.DevBot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUD {

    public static void createTable() throws SQLException {

        String sql = "create table tb_guild\n" +
                "                (\n" +
                "                    id integer not null primary key autoincrement unique,\n" +
                "                    guild_id text not null unique,\n" +
                "                    prefix text not null,\n" +
                "                    autorole text\n" +
                "                )";
        PreparedStatement statement = ConnectionFactory.getConn().prepareStatement(sql);
        statement.execute();
        statement.close();
        ConnectionFactory.getConn().close();
    }


    public static void insert(String guildId, char prefix) throws SQLException {
        String sql = "insert or ignore into tb_guild values (null, ?, ?, null)";
        PreparedStatement statement = ConnectionFactory.getConn().prepareStatement(sql);
        statement.setString(1, guildId);
        statement.setString(2, String.valueOf(prefix));
        statement.execute();
        statement.close();
        ConnectionFactory.getConn().close();
    }


    public static void select(String guildId) throws SQLException {
        String sql = "select * from tb_guild where guild_id = ?";
        PreparedStatement statement = ConnectionFactory.getConn().prepareStatement(sql);
        statement.setString(1, guildId);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            DevBot.prefixMap.put(guildId, resultSet.getString("prefix").charAt(0));
            DevBot.autoroleMap.put(guildId, resultSet.getString("autorole"));
        }
        statement.close();
        ConnectionFactory.getConn().close();
    }

    public static void update(String cln, String guildId, String value) throws SQLException {
        String sql = String.format("update tb_guild set %s = ? where guild_id = ?", cln);
        PreparedStatement statement = ConnectionFactory.getConn().prepareStatement(sql);
        statement.setString(1, value);
        statement.setString(2, guildId);
        statement.executeUpdate();
        statement.close();
        ConnectionFactory.getConn().close();
    }
}
