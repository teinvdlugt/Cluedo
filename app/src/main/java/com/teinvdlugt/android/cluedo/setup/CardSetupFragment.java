package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.teinvdlugt.android.cluedo.Category;
import com.teinvdlugt.android.cluedo.Game;
import com.teinvdlugt.android.cluedo.MainActivity;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class CardSetupFragment extends Fragment {

    LinearLayout categoriesContainer;
    ArrayList<CardSetupCategoryLayout> categories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.game = new Game();

        View theView = inflater.inflate(R.layout.fragment_card_setup, container, false);
        categoriesContainer = (LinearLayout) theView.findViewById(R.id.categories_linearLayout);

        theView.findViewById(R.id.addCategory_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
        theView.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

        addCategory();
        return theView;
    }

    private void addCategory() {
        final CardSetupCategoryLayout newCategory = new CardSetupCategoryLayout(getContext());
        categories.add(newCategory);
        categoriesContainer.addView(newCategory);
        newCategory.setOnCategoryDeleteClickListener(new CardSetupCategoryLayout.OnCategoryDeleteClickListener() {
            @Override
            public void onCategoryClickDelete(CardSetupCategoryLayout view) {
                categories.remove(newCategory);
                categoriesContainer.removeView(newCategory);
            }
        });
    }

    private void done() {
        ArrayList<Category> result = new ArrayList<>();
        for (CardSetupCategoryLayout categoryLayout : categories)
            result.add(categoryLayout.getCategory(MainActivity.game));
        // TODO: 15-9-2015 Pass MainActivity.game to this Fragment via newInstance() method instead of directly and statically referencing it
        listener.onCardsChosen(result);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnCardsChosenListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCardsChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private OnCardsChosenListener listener;

    public interface OnCardsChosenListener {
        void onCardsChosen(ArrayList<Category> categories);
    }
}