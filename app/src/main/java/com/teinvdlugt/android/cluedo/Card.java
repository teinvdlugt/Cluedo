package com.teinvdlugt.android.cluedo;


import java.util.ArrayList;

public class Card {

    private String name;
    private Game game;

    public Card(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public Card(String name) {
        this.name = name;
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

    public Player owner() {
        for (Player player : game.players)
            if (player.allPossessionsEqual(Possession.OWNS, this))
                return player;
        return null;
    }

    public Player[] dontOwn() {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : game.players)
            if (player.allPossessionsEqual(Possession.DOESNTOWN, this))
                players.add(player);

        Player[] result = new Player[players.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = players.get(i);
        }
        return result;
    }

    public static String[] names(Card[] cards) {
        String[] names = new String[cards.length];
        for (int i = 0; i < names.length; i++)
            names[i] = cards[i].name;

        return names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
