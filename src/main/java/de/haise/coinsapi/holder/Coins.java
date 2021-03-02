package de.haise.coinsapi.holder;

import java.util.UUID;

public class Coins {

    private UUID uuid;
    private Integer coins;

    public UUID getUuid() {
        return uuid;
    }

    public int getCoins() {
        return coins;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

}

