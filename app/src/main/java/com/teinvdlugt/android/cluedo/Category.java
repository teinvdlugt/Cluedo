package com.teinvdlugt.android.cluedo;


public class Category {

    private String name;
    private Card[] cards;
    private Card prime;

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
