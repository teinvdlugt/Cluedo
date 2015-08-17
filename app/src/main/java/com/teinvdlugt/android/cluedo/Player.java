package com.teinvdlugt.android.cluedo;


import java.util.ArrayList;

public class Player {

    private String name;
    private Game game;
    private ArrayList<Possession> possessions = new ArrayList<>();
    private ArrayList<ArrayList<Card>> chances = new ArrayList<>();

    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public void setDoesntOwn(Card... cards) {
        for (Card card : cards) {
            // Delete the possessions of the card which already exist
            for (int i = 0; i < possessions.size(); i++) {
                if (possessions.get(i).card.equals(card)) {
                    possessions.remove(i);
                }
            }

            // Add a possession indicating that this player doesn't own the card
            possessions.add(new Possession(card, Possession.DOESNTOWN));

            // Now check for chances concerning this card.
            // If there is a chance concerning this card, remove the card
            // from the chance ArrayList and look if there is only one card
            // in that chance ArrayList left.

            for (ArrayList<Card> chance : chances) {
                if (chance.contains(card)) {
                    // This is a chance concerning this card.
                    chance.remove(card);
                    if (chance.size() == 1) {
                        setOwns(chance.get(0));
                    }
                }
            }
        }
    }

    public void setOwns(Card card) {
        // Delete the possessions of the card which already exist
        for (int i = 0; i < possessions.size(); i++) {
            if (possessions.get(i).card.equals(card)) {
                possessions.remove(i);
            }
        }

        // Also delete chances involving this card.
        for (int i = 0; i < chances.size(); i++) {
            if (chances.get(i).contains(card)) {
                chances.remove(i);
            }
        }

        // Add a possession indicating that this player owns the card
        possessions.add(new Possession(card, Possession.OWNS));

        // For all other players, add a possession indicating that they don't
        // own the card (since this player already owns it).
        for (Player player : game.players) {
            if (!player.equals(this)) {
                player.setDoesntOwn(card);
            }
        }
    }

    public void addChance(ArrayList<Card> chance) {
        if (chance.size() == 1) {
            setOwns(chance.get(0));
            return;
        }

        chances.add(chance);
    }

    /**
     * For example: if you want to know if the player owns all of
     * a few cards, call {@code allPossessionsEqual(Possession.OWNS, card1, card2, card3)}
     *
     * @param status The possession status you want to check. Must be one of
     *               Possession.OWNS, Possession.DOESNTOWN or Possession.UNKNOWN.
     * @param cards  The cards you want to check.
     * @return True if the possessions of all the cards equal the passed status, false if not
     */
    public boolean allPossessionsEqual(int status, Card... cards) {
        ArrayList<Possession> cardPossessions = filterPossessions(cards);

        if (cardPossessions.size() != cards.length) return false;

        for (Possession possession : cardPossessions)
            if (possession.status != status) return false;

        return true;
    }

    public boolean anyPossessionEquals(int status, Card... cards) {
        ArrayList<Possession> cardPossessions = filterPossessions(cards);

        for (Possession possession : cardPossessions)
            if (possession.status == status) return true;

        return false;
    }

    public ArrayList<Possession> filterPossessions(Card... cards) {
        // An array of Possessions just like the field "possessions" but then
        // filtered out for only the cards passed to this method
        ArrayList<Possession> cardPossessions = new ArrayList<>();

        for (Card card : cards) {
            for (Possession possession : possessions) {
                if (possession.card.equals(card)) {
                    cardPossessions.add(possession);
                    break;
                }
            }
        }

        return cardPossessions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
