package me.thiagocodex.devbot.database;

import me.thiagocodex.devbot.main.DevBot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class Config {

    public static final File parentJarLocation = new File(
            DevBot.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();

    public static File databaseFile = new File(parentJarLocation + "/devbot.db");

    public static void createFilesAndTable() throws IOException, SQLException {

        if (Files.notExists(databaseFile.toPath())) {
            Files.createFile(databaseFile.toPath());
            CRUD.createTable();
        }
    }
}
