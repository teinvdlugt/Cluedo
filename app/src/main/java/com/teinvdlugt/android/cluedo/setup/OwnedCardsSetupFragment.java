package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.teinvdlugt.android.cluedo.Card;
import com.teinvdlugt.android.cluedo.LaunchActivity;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class OwnedCardsSetupFragment extends Fragment {

    private CategoryTreeView treeView;
    private OnOwnedCardsChosenListener listener;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_owned_cards_setup, container, false);

        treeView = (CategoryTreeView) theView.findViewById(R.id.treeView);
        scrollView = (ScrollView) theView.findViewById(R.id.scrollView);
        ViewGroup root = (ViewGroup) theView.findViewById(R.id.root);

        theView.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

        LaunchActivity.setMaxWidth(root, 800);
        return theView;
    }

    private void done() {
        ArrayList<Card> ownedCards = treeView.getSelectedCards();

        if (ownedCards.isEmpty()) {
            Snackbar.make(scrollView, R.string.R_string_owned_cards_setup_error, Snackbar.LENGTH_LONG).show();
        } else if (listener != null) listener.onOwnedCardsChosen(ownedCards);
    }


    public interface OnOwnedCardsChosenListener {
        void onOwnedCardsChosen(ArrayList<Card> ownedCards);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnOwnedCardsChosenListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnOwnedCardsChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
