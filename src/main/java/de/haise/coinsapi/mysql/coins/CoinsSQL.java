package de.haise.coinsapi.mysql.coins;

import de.haise.coinsapi.events.CoinChangeEvent;
import de.haise.coinsapi.holder.Coins;
import de.haise.coinsapi.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CoinsSQL {

    private Connection connection;
    private UUID uuid;

    public CoinsSQL(UUID uuid) {
        this.connection = MySQL.con;
        this.uuid = uuid;
    }

    public void create() {
        if (!(exists())) {
            MySQL.update("INSERT INTO Coins(UUID, COINS) VALUES ('" + uuid.toString() + "', '0');");
        }
    }

    public boolean exists() {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM Coins WHERE UUID= '" + uuid.toString() + "'");
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            rs.close();
            st.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setCoins(int amount) {
        if (exists()) {
            MySQL.update("UPDATE Coins SET COINS= '" + amount + "' WHERE UUID= '" + uuid.toString() + "';");
            Player player = Bukkit.getPlayer(uuid);
            Bukkit.getPluginManager().callEvent(new CoinChangeEvent(player));
        } else {
            create();
            setCoins(amount);
        }
    }

    public void removeCoins(Integer amount) {
        if (exists()) {
            get(new Consumer<Coins>() {
                @Override
                public void accept(Coins coins) {
                    setCoins(coins.getCoins() - amount.intValue());
                }
            });
            Player player = Bukkit.getPlayer(uuid);
            Bukkit.getPluginManager().callEvent(new CoinChangeEvent(player));
        } else {
            create();
            setCoins(amount);
        }
    }

    public void addCoins(Integer amount) {
        if (exists()) {
            get(new Consumer<Coins>() {
                @Override
                public void accept(Coins coins) {
                    setCoins(coins.getCoins() + amount.intValue());
                }
            });
            Player player = Bukkit.getPlayer(uuid);
            Bukkit.getPluginManager().callEvent(new CoinChangeEvent(player));
        } else {
            create();
            setCoins(amount);
        }
    }

    public void get(Consumer<Coins> consumer) {
        Executors.newFixedThreadPool(2).execute(() -> {
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Coins WHERE UUID='" + uuid.toString() + "'")) {
                if(!exists()) {
                    get(consumer);
                }else{
                    ResultSet resultSet = preparedStatement.executeQuery();
                    Coins coins = new Coins();

                    while(resultSet.next()) {

                        coins.setUuid(UUID.fromString(resultSet.getString("UUID")));
                        coins.setCoins(resultSet.getInt("COINS"));
                        consumer.accept(coins);

                    }
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

}
