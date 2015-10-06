package com.teinvdlugt.android.cluedo.io;

import android.content.Context;

import com.teinvdlugt.android.cluedo.Card;
import com.teinvdlugt.android.cluedo.Category;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class CardSetup {
    public static final String DEFAULT = "Default";

    private String name;
    private ArrayList<Category> categories;

    public CardSetup(String name, ArrayList<Category> categories) {
        this.name = name;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public static CardSetup defaultSetup(Context context) {
        CardSetup setup = new CardSetup(context.getString(R.string.default_setup), new ArrayList<Category>());
        setup.categories.add(new Category("Suspects",
                new Card("Col. Mustard"),
                new Card("Prof. Plum"),
                new Card("Rev. Green"),
                new Card("Mrs. Peacock"),
                new Card("Miss Scarlett"),
                new Card("Mrs. White")));
        setup.categories.add(new Category("Weapons",
                new Card("Dagger"),
                new Card("Candlestick"),
                new Card("Revolver"),
                new Card("Rope"),
                new Card("Lead Pipe"),
                new Card("Spanner")));
        setup.categories.add(new Category("Rooms",
                new Card("Hall"),
                new Card("Lounge"),
                new Card("Dining Room"),
                new Card("Kitchen"),
                new Card("Ballroom"),
                new Card("Conservatory"),
                new Card("Billiard Room"),
                new Card("Library"),
                new Card("Study")));
        return setup;
    }
}
