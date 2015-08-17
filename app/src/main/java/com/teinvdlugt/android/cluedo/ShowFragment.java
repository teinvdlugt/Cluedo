package com.teinvdlugt.android.cluedo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Activities that contain this fragment must implement the
 * {@link ShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShowFragment extends Fragment implements ShowRecyclerAdapter.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<Player> hadNothing = new ArrayList<>();
    private Player showed;
    private Card cardShowed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_show, container, false);

        recyclerView = (RecyclerView) theView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ShowRecyclerAdapter(getActivity(),
                MainActivity.game.nextPlayerClockwise(MainActivity.game.getPlayerAtTurn()), this));

        return theView;
    }

    @Override
    public void onClickYes(Player player) {
        showed = player;

        if (MainActivity.game.getPlayerAtTurn().equals(MainActivity.game.getAppUser())) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Which card does " + showed.getName() + " show?")
                    .setItems(Card.names(MainActivity.chosenCards), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cardShowed = MainActivity.chosenCards[which];
                            returnToActivity();
                        }
                    }).create().show();
        } else {
            returnToActivity();
        }
    }

    @Override
    public void onClickNo(Player player) {
        hadNothing.add(player);

        if (MainActivity.game.nextPlayerClockwise(player).equals(MainActivity.game.getPlayerAtTurn())) {
            showed = null;
            returnToActivity();
        } else {
            ((ShowRecyclerAdapter) recyclerView.getAdapter()).addPlayerToList(
                    MainActivity.game.nextPlayerClockwise(player));
        }
    }

    public void returnToActivity() {
        if (mListener != null) {
            Player[] hadNothing1 = new Player[hadNothing.size()];
            for (int i = 0; i < hadNothing.size(); i++) {
                hadNothing1[i] = hadNothing.get(i);
            }
            mListener.onCardShowed(hadNothing1, showed, cardShowed);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        /**
         * Fragment's callback to activity.
         *
         * @param hadNothing An array of Players who were asked to show a card but
         *                   didn't have one of the cards that were suspected by
         *                   the player at turn. If the first person already showed
         *                   something, pass null.
         * @param showed     The player that showed one of his/her cards. If nobody
         *                   showed anything, pass null.
         * @param cardShowed The card that was shown by Player showed. This is only
         *                   known when it's the turn of the {@code game.appUser}. If not,
         *                   pass null.
         */
        void onCardShowed(@Nullable Player[] hadNothing, @Nullable Player showed, @Nullable Card cardShowed);
    }
}

class ShowRecyclerAdapter extends RecyclerView.Adapter<ShowRecyclerAdapter.ShowViewHolder> {
    private ArrayList<Player> data = new ArrayList<>();
    private OnClickListener mListener;
    private Context context;

    public ShowRecyclerAdapter(Context context, Player firstToShow, OnClickListener listener) {
        this.context = context;
        this.mListener = listener;
        data.add(firstToShow);
    }

    public void addPlayerToList(Player player) {
        data.add(player);
        //notifyItemInserted(data.size()); // TODO: 9-8-2015 For animations, but buttons don't get disabled
        notifyDataSetChanged();
    }

    interface OnClickListener {
        void onClickYes(Player player);

        void onClickNo(Player player);
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_show, parent, false);
        return new ShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShowViewHolder holder, final int i) {
        boolean disableButtons = i != data.size() - 1;
        if (!data.get(i).equals(MainActivity.game.getAppUser())) {
            holder.setUpNormalCard(data.get(i), disableButtons);
        } else {
            ArrayList<Card> appUserHas = new ArrayList<>();
            for (Card card : MainActivity.chosenCards)
                if (MainActivity.game.getAppUser().allPossessionsEqual(Possession.OWNS, card))
                    appUserHas.add(card);

            if (appUserHas.size() == 0) {
                // App user doesn't have any of the cards
                holder.setUpAppUserDoesNotHave(disableButtons);
            } else {
                // App user has got one or more of the cards
                holder.setUpAppUserShows(appUserHas, disableButtons);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder {
        LinearLayout normal, appUserShows, appUserDoesNotHave;

        public ShowViewHolder(View itemView) {
            super(itemView);

            normal = (LinearLayout) itemView.findViewById(R.id.normal_show_card);
            appUserShows = (LinearLayout) itemView.findViewById(R.id.appUser_shows);
            appUserDoesNotHave = (LinearLayout) itemView.findViewById(R.id.appUserDoesNotHave);
        }

        public void setUpNormalCard(final Player player, boolean disableButtons) {
            appUserDoesNotHave.setVisibility(View.GONE);
            appUserShows.setVisibility(View.GONE);
            normal.setVisibility(View.VISIBLE);

            Button yesButton = (Button) normal.findViewById(R.id.yesButton);
            Button noButton = (Button) normal.findViewById(R.id.noButton);

            ((TextView) normal.findViewById(R.id.title)).setText("Did " + player.getName() + " show something?");
            if (disableButtons) {
                yesButton.setEnabled(false);
                noButton.setEnabled(false);
            } else {
                yesButton.setEnabled(true);
                noButton.setEnabled(true);
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) mListener.onClickNo(player);
                    }
                });
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) mListener.onClickYes(player);
                    }
                });
            }
        }

        public void setUpAppUserDoesNotHave(boolean disableButton) {
            appUserDoesNotHave.setVisibility(View.VISIBLE);
            appUserShows.setVisibility(View.GONE);
            normal.setVisibility(View.GONE);

            ((TextView) appUserDoesNotHave.findViewById(R.id.textView)).setText("You don't have a card to show.");
            Button button = (Button) appUserDoesNotHave.findViewById(R.id.okButton);
            if (disableButton) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) mListener.onClickNo(MainActivity.game.getAppUser());
                    }
                });
            }
        }

        public void setUpAppUserShows(ArrayList<Card> cardsToDisplay, boolean disableButton) {
            appUserDoesNotHave.setVisibility(View.GONE);
            appUserShows.setVisibility(View.VISIBLE);
            normal.setVisibility(View.GONE);

            ((TextView) appUserShows.findViewById(R.id.title2)).setText(
                    "Show " + MainActivity.game.getPlayerAtTurn().getName() + " one of these cards:");
            TextView cardsTV = (TextView) appUserShows.findViewById(R.id.cardsToShow);
            cardsTV.setText(cardsToDisplay.get(0).getName());
            for (int i = 1; i < cardsToDisplay.size(); i++) {
                cardsTV.append(", " + cardsToDisplay.get(i).getName());
            }

            Button button = (Button) appUserShows.findViewById(R.id.doneButton);
            if (disableButton) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) mListener.onClickYes(MainActivity.game.getAppUser());
                    }
                });
            }

        }
    }
}
