package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teinvdlugt.android.cluedo.Card;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class OwnedCardsSetupFragment extends Fragment {

    private CategoryTreeView treeView;
    private OnOwnedCardsChosenListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_owned_cards_setup, container, false);

        treeView = (CategoryTreeView) theView.findViewById(R.id.treeView);

        theView.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

        return theView;
    }

    private void done() {
        ArrayList<Card> ownedCards = treeView.getSelectedCards();
        if (listener != null) listener.onOwnedCardsChosen(ownedCards);
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
