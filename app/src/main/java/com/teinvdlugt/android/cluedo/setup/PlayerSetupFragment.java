package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.teinvdlugt.android.cluedo.LaunchActivity;
import com.teinvdlugt.android.cluedo.MainActivity;
import com.teinvdlugt.android.cluedo.Player;
import com.teinvdlugt.android.cluedo.R;
import com.teinvdlugt.android.cluedo.io.PlayerSetupJSONUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerSetupFragment extends Fragment implements PlayerSetupView.OnPlayerNameClickListener {

    private LinearLayout playerETContainer;
    private ArrayList<EditText> playerETs = new ArrayList<>();
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_setup, container, false);

        playerETContainer = (LinearLayout) view.findViewById(R.id.playerETContainer);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        ViewGroup root = (ViewGroup) view.findViewById(R.id.root);

        view.findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
        view.findViewById(R.id.addPlayer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayerET();
            }
        });
        ((PlayerSetupView) view.findViewById(R.id.players_list)).setOnPlayerNameClickListener(this);

        addPlayerET();
        LaunchActivity.setMaxWidth(root, 800);
        return view;
    }

    private void addPlayerET() {
        final ViewGroup editTextLayout = (ViewGroup) LayoutInflater.from(getContext())
                .inflate(R.layout.view_card_setup_edit_text, playerETContainer, false);
        final EditText et = (EditText) editTextLayout.findViewById(R.id.editText);
        final View deleteButton = editTextLayout.findViewById(R.id.deleteButton);

        et.setHint(getString(R.string.name_of_player));
        playerETs.add(et);
        playerETContainer.addView(editTextLayout);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerETs.remove(et);
                playerETContainer.removeView(editTextLayout);
            }
        });
    }

    @Override
    public void onPlayerNameClick(String name) {
        for (EditText et : playerETs) {
            if (et.length() == 0) {
                playerETs.get(playerETs.size() - 1).setText(name);
                return;
            }
        }

        addPlayerET();
        playerETs.get(playerETs.size() - 1).setText(name);
    }

    private void done() {
        // Create list of player names
        Set<String> names = new HashSet<>();
        for (EditText playerET : playerETs) {
            String name = playerET.getText().toString();
            if (name.length() > 0)
                names.add(name);
        }
        if (names.size() == 0) {
            Snackbar.make(scrollView, R.string.player_setup_error_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        // Create Player objects
        ArrayList<Player> players = new ArrayList<>();
        for (String name : names) {
            players.add(new Player(name, MainActivity.game, 4));
        }

        // Save names of players
        PlayerSetupJSONUtils.addPlayers(getContext(), names);

        // Notify listener
        if (listener != null)
            listener.onPlayersChosen(new Player("You", MainActivity.game, 4), players);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnPlayersChosenListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnPlayersChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private OnPlayersChosenListener listener;

    public interface OnPlayersChosenListener {
        void onPlayersChosen(Player appUser, ArrayList<Player> players);
    }
}
