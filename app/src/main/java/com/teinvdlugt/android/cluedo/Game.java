package com.teinvdlugt.android.cluedo;


import java.util.ArrayList;
import java.util.Collections;

public class Game {

    public final ArrayList<Player> players = new ArrayList<>();
    public final ArrayList<Category> categories = new ArrayList<>();

    private Player playerAtTurn;

    public void turn(Player[] hadNothing, Player showed, Card... cards) {
        // If some people said they had nothing:
        if (hadNothing != null && hadNothing.length > 0) {
            for (Player player : hadNothing) player.setDoesntOwn(cards);
        }

        // If everyone said they had nothing:
        if (showed == null) {
            if (playerAtTurn.allPossessionsEqual(Possession.DOESNTOWN, cards)) {
                // Nobody, not even the player at turn himself, has one or more of the cards.
                // TODO End of the game.
            } else if (playerAtTurn.allPossessionsEqual(Possession.OWNS, cards)) {
                // TODO This player is very silly.
            } else {
                // The player at turn owns some of the cards. The other cards become prime suspects.

                for (int i = 0; i < cards.length; i++) {
                    if (playerAtTurn.allPossessionsEqual(Possession.DOESNTOWN, cards[i])) {
                        // This card becomes prime suspect.
                        categories.get(i).setPrime(cards[i]);
                    }
                }
            }
        } else {
            // Someone showed something.

            // If the player who showed something is known to have one of the cards,
            // there is nothing new to deduce.
            if (playerAtTurn.anyPossessionEquals(Possession.OWNS, cards)) return;

            final ArrayList<Card> chance = new ArrayList<>();
            Collections.addAll(chance, cards);
            // Filter out the cards which the player who showed something can't have.
            for (Card card : cards) {
                // If the card is prime suspect or if the player is already known to not own the card
                if (card.isPrime() || showed.allPossessionsEqual(Possession.DOESNTOWN, card))
                    chance.remove(card);
                else
                    // Or if the card is owned by someone else
                    for (Player player : players)
                        if (!showed.equals(player) && player.allPossessionsEqual(Possession.OWNS, card))
                            chance.remove(card);
            }
            if (chance.size() == 0) {
                // TODO show error
            } else {
                showed.addChance(chance);
            }
        }

        checkForPrimes();

        if (checkEndOfGame()) {
            // TODO show happy message
        }
    }

    public void checkForPrimes() {
        for (Category cat : categories) {
            if (cat.getPrime() != null) break;

            // Check for each card if it is owned by no one
            for (Card card : cat.getCards()) {
                if (card.ownedByNoOne()) {
                    cat.setPrime(card);
                    // TODO: 5-8-2015 In cat.setPrime, should methods be called to indicate that none of the players owns the card?
                    // In that case chances must be checked again and primes must be checked again...

                    // TODO show happy message
                    break;
                }
            }

            // Check for each category if there is only one card not owned by anyone
            ArrayList<Card> unknownCards = new ArrayList<>();
            Collections.addAll(unknownCards, cat.getCards());
            for (Card card : cat.getCards()) {
                if (card.isOwned()) unknownCards.remove(card);
            }
            if (unknownCards.size() == 1) {
                cat.setPrime(unknownCards.get(0));
                // TODO show happy message
            }
        }
    }

    public boolean checkEndOfGame() {
        for (Category category : categories) {
            if (category.getPrime() == null) return false;
        }
        return true;
    }

    public void setPlayerAtTurn(Player playerAtTurn) {
        this.playerAtTurn = playerAtTurn;
    }

    public Player getPlayerAtTurn() {
        return playerAtTurn;
    }

    public Player nextPlayerClockwise(Player reference) {
        if (reference == null) {
            return null;
        } else if (players.indexOf(reference) == players.size() - 1) {
            return players.get(0);
        } else {
            return players.get(players.indexOf(reference) + 1);
        }
    }
}
