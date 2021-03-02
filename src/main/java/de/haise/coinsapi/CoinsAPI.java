package de.haise.coinsapi;

import de.haise.coinsapi.mysql.MySQL;
import de.haise.coinsapi.mysql.MySQLFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CoinsAPI extends JavaPlugin {

    @Getter
    private static CoinsAPI instance;

    @Override
    public void onLoad() {
        instance = this;
        MySQL.connect();
        MySQLFile.setStandard();
        MySQLFile.readData();
        System.out.println("[CoinsAPI] API Loaded");
    }

    @Override
    public void onEnable() {
        System.out.println("[CoinsAPI] API Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[CoinsAPI] API Disabled");
        instance = null;
        MySQL.disconnect();
    }

}
