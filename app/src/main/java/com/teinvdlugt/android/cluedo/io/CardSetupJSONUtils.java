package com.teinvdlugt.android.cluedo.io;


import android.content.Context;
import android.support.annotation.NonNull;

import com.teinvdlugt.android.cluedo.Card;
import com.teinvdlugt.android.cluedo.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class CardSetupJSONUtils {
    private static final String CARD_SETUPS_FILE = "game_setups";
    private static final String CARD_SETUP_NAME = "\"name\"";
    private static final String CARD_SETUP_CATEGORIES = "\"categories\"";
    private static final String CATEGORY_NAME = "\"category_name\"";
    private static final String CATEGORY_CARDS = "\"cards\"";
    private static final String CARD_NAME = "\"card_name\"";

    public static void saveCardSetup(Context context, @NonNull String name, ArrayList<Category> categories) {
        ArrayList<CardSetup> saved = getSavedCardSetups(context);

        // Remove duplicates
        for (Iterator<CardSetup> it = saved.iterator(); it.hasNext(); )
            if (name.equals(it.next().getName()))
                it.remove();

        // Add new one
        saved.add(new CardSetup(name, categories));

        String json = cardSetupArrayToJSON(saved);
        FileUtils.saveFile(context, CARD_SETUPS_FILE, json);
    }

    private static String cardSetupArrayToJSON(ArrayList<CardSetup> array) {
        StringBuilder json = new StringBuilder("[");
        for (CardSetup setup : array)
            json.append(cardSetupToJSON(setup)).append(",");
        json.deleteCharAt(json.length() - 1);
        return json.append("]").toString();
    }

    private static String cardSetupToJSON(CardSetup setup) {
        final String QUOTE = "\"";
        StringBuilder json = new StringBuilder("{")
                .append(CARD_SETUP_NAME).append(":").append(QUOTE).append(setup.getName()).append("\",")
                .append(CARD_SETUP_CATEGORIES).append(":[");
        for (Category cat : setup.getCategories()) {
            json.append("{").append(CATEGORY_NAME).append(":").append(QUOTE).append(cat.getName()).append("\",")
                    .append(CATEGORY_CARDS).append(":[");
            for (Card card : cat.getCards()) {
                json.append("{").append(CARD_NAME).append(":").append(QUOTE).append(card.getName()).append("\"},");
            }
            // Delete last comma
            json.deleteCharAt(json.length() - 1);
            json.append("]},");
        }
        // Delete last comma
        json.deleteCharAt(json.length() - 1);
        json.append("]}");

        return json.toString();
    }

    public static ArrayList<String> getSavedCardSetupNames(Context context) {
        String file = FileUtils.loadFile(context, CARD_SETUPS_FILE);
        try {
            JSONArray jsonArray = new JSONArray(file);
            ArrayList<String> result = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(jsonArray.getJSONObject(i).getString(CARD_SETUP_NAME));
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static ArrayList<CardSetup> getSavedCardSetups(Context context) {
        ArrayList<CardSetup> result = new ArrayList<>();
        String file = FileUtils.loadFile(context, CARD_SETUPS_FILE);
        try {
            JSONArray jArray = new JSONArray(file);

            // CardSetups
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String name = jObject.getString(CARD_SETUP_NAME);
                JSONArray jCategories = jObject.getJSONArray(CARD_SETUP_CATEGORIES);
                ArrayList<Category> categories = new ArrayList<>();

                // Categories
                for (int j = 0; j < jCategories.length(); j++) {
                    JSONObject jCategory = jCategories.getJSONObject(i);
                    String categoryName = jCategory.getString(CATEGORY_NAME);
                    JSONArray jCards = jCategory.getJSONArray(CATEGORY_CARDS);
                    Card[] cards = new Card[jCards.length()];

                    // Cards
                    for (int k = 0; k < cards.length; k++) {
                        String cardName = jCards.getJSONObject(k).getString(CARD_NAME);
                        cards[k] = new Card(cardName);
                    }
                    categories.add(new Category(categoryName, cards));
                }
                result.add(new CardSetup(name, categories));
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            result.clear();
        }

        return result;
    }

}
