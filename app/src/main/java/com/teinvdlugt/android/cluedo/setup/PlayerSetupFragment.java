package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.teinvdlugt.android.cluedo.MainActivity;
import com.teinvdlugt.android.cluedo.Player;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class PlayerSetupFragment extends Fragment {

    private LinearLayout playerETContainer;
    private ArrayList<EditText> playerETs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_setup, container, false);

        playerETContainer = (LinearLayout) view.findViewById(R.id.playerETContainer);

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

        addPlayerET();
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

    private void done() {
        ArrayList<Player> players = new ArrayList<>();
        for (EditText playerET : playerETs) {
            players.add(new Player(playerET.getText().toString(), MainActivity.game, 4));
        }
        if (listener != null) listener.onPlayersChosen(new Player("You", MainActivity.game, 4), players);
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
