package com.teinvdlugt.android.cluedo;


public class Possession {
    public static final int OWNS = 1;
    public static final int DOESNTOWN = 0;
    public static final int UNKNOWN = 2;

    public final Card card;
    public final int status;

    public Possession(Card card, int status) {
        this.card = card;
        this.status = status;
    }
}
