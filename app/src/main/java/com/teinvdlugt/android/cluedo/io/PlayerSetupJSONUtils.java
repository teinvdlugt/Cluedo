package com.teinvdlugt.android.cluedo.io;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class PlayerSetupJSONUtils {
    public static final String PLAYERS_FILE = "players";
    public static final String PLAYER_NAME = "name";

    public static void addPlayers(Context context, Set<String> names) {
        if (names.size() == 0) return;

        List<String> savedNames = getPlayers(context);

        for (String name : names)
            if (!savedNames.contains(name) && !name.isEmpty())
                savedNames.add(name);

        savePlayers(context, savedNames);
    }

    public static void savePlayers(Context context, List<String> names) {
        StringBuilder json = new StringBuilder("[");
        for (String name : names) {
            json.append("{\"").append(PLAYER_NAME).append("\":\"").append(name).append("\"},");
        }
        // Remove last comma
        json.deleteCharAt(json.length() - 1);

        FileUtils.saveFile(context, PLAYERS_FILE, json.append("]").toString());
    }

    public static ArrayList<String> getPlayers(Context context) {
        String file = FileUtils.loadFile(context, PLAYERS_FILE);
        try {
            JSONArray jArray = new JSONArray(file);
            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++) {
                result.add(jArray.getJSONObject(i).getString(PLAYER_NAME));
            }

            return result;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void deletePlayer(Context context, String name) {
        if (name == null) return;

        List<String> names = getPlayers(context);
        for (Iterator<String> it = names.iterator(); it.hasNext(); )
            if (name.equals(it.next()))
                it.remove();

        savePlayers(context, names);
    }
}
