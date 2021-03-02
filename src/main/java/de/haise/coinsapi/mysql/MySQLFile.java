package de.haise.coinsapi.mysql;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MySQLFile {

    public static void setStandard() {
        FileConfiguration cfg = getFileConfiguration();

        cfg.options().copyDefaults(true);
        cfg.addDefault("host", "localhost");
        cfg.addDefault("database", "database");
        cfg.addDefault("username", "username");
        cfg.addDefault("password", "password");

        try {
            cfg.save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getFile() {
        return new File("CoinAPI/mysql", "MySQL.yml");
    }

    private static FileConfiguration getFileConfiguration() {
        return YamlConfiguration.loadConfiguration(getFile());
    }

    public static void readData() {
        FileConfiguration cfg = getFileConfiguration();

        MySQL.host = cfg.getString("host");
        MySQL.database = cfg.getString("database");
        MySQL.username = cfg.getString("username");
        MySQL.password = cfg.getString("password");
    }


}
