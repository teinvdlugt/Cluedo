package com.teinvdlugt.android.cluedo;


public class Card {

    private String name;
    private Game game;

    public Card(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public boolean isPrime() {
        for (Category category : game.categories)
            if (this.equals(category.getPrime()))
                return true;
        return false;
    }

    public boolean ownedByNoOne() {
        for (Player player : game.players)
            if (!player.allPossessionsEqual(Possession.DOESNTOWN, this))
                return false;
        return true;
    }

    public boolean isOwned() {
        for (Player player : game.players)
            if (player.allPossessionsEqual(Possession.OWNS, this))
                return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
