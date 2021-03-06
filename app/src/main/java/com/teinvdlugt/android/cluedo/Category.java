package com.teinvdlugt.android.cluedo;


public class Category {

    private String name;
    private Card[] cards;
    private Card prime;

    public Category(String name, Card... cards) {
        this.name = name;
        this.cards = cards;
    }

    public String[] cardNames() {
        String[] names = new String[cards.length];
        for (int i = 0; i < cards.length; i++) {
            names[i] = cards[i].getName();
        }
        return names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public Card getPrime() {
        return prime;
    }

    public void setPrime(Card prime) {
        this.prime = prime;
    }
}
