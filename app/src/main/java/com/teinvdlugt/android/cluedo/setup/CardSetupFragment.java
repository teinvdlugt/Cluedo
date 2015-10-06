package com.teinvdlugt.android.cluedo.setup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.teinvdlugt.android.cluedo.Category;
import com.teinvdlugt.android.cluedo.Game;
import com.teinvdlugt.android.cluedo.MainActivity;
import com.teinvdlugt.android.cluedo.R;
import com.teinvdlugt.android.cluedo.io.CardSetup;
import com.teinvdlugt.android.cluedo.io.SetupJSONHandler;

import java.util.ArrayList;

public class CardSetupFragment extends Fragment {

    private LinearLayout categoriesContainer;
    private ArrayList<CardSetupCategoryLayout> categories = new ArrayList<>();
    private EditText cardSetupNameET;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.game = new Game();

        View theView = inflater.inflate(R.layout.fragment_card_setup, container, false);
        categoriesContainer = (LinearLayout) theView.findViewById(R.id.categories_linearLayout);
        cardSetupNameET = (EditText) theView.findViewById(R.id.card_setup_name_editText);

        theView.findViewById(R.id.addCategory_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
        theView.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNext();
            }
        });
        theView.findViewById(R.id.loadExisting_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLoadFromExisting();
            }
        });
        theView.findViewById(R.id.clearAll_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClearAll();
            }
        });

        return theView;
    }

    private CardSetupCategoryLayout addCategory() {
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
        return newCategory;
    }

    private void onClickLoadFromExisting() {
        final ArrayList<CardSetup> cardSetups = SetupJSONHandler.getSavedCardSetups(getContext());
        if (cardSetups.size() == 0) cardSetups.add(CardSetup.defaultSetup(getContext()));

        // No saved card setups:
        if (cardSetups.size() == 0) {
            new AlertDialog.Builder(getContext())
                    .setMessage("You don't have any saved game setups.")
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
            return;
        }

        // Names of the card setups:
        String[] names = new String[cardSetups.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = cardSetups.get(i).getName();
        }

        // Show the dialog:
        new AlertDialog.Builder(getContext())
                .setTitle("Choose existing game setup")
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadCardSetup(cardSetups.get(which));
                    }
                }).setNeutralButton(android.R.string.cancel, null)
                .create().show();
    }

    private void loadCardSetup(CardSetup setup) {
        categoriesContainer.removeAllViews();
        categories.clear();

        for (Category cat : setup.getCategories()) {
            CardSetupCategoryLayout catLayout = addCategory();
            catLayout.loadCategory(cat);
        }
        cardSetupNameET.setText(setup.getName());
    }

    private void onClickNext() {
        if (!validInput()) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Please enter valid values.")
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
            return;
        }

        String cardSetupName = cardSetupNameET.getText().toString();

        // Parse the categories
        ArrayList<Category> result = new ArrayList<>();
        for (CardSetupCategoryLayout categoryLayout : categories)
            result.add(categoryLayout.getCategory(MainActivity.game));

        // Save this setup
        SetupJSONHandler.saveCardSetup(getContext(), cardSetupName, result);

        // Notify MainActivity
        listener.onCardsChosen(result);
    }

    private void onClickClearAll() {
        categories.clear();
        categoriesContainer.removeAllViews();
        cardSetupNameET.setText("");
    }

    private boolean validInput() {
        if (cardSetupNameET.length() == 0) return false;
        if (categories.size() == 0) return false;
        for (CardSetupCategoryLayout category : categories) {
            if (!category.validInput()) return false;
        }
        return true;
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