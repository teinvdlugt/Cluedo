package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.teinvdlugt.android.cluedo.Card;
import com.teinvdlugt.android.cluedo.Category;
import com.teinvdlugt.android.cluedo.Game;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class CardSetupCategoryLayout extends LinearLayout {
    private EditText categoryET;
    private LinearLayout cardETContainer;
    private ArrayList<EditText> cardETs = new ArrayList<>();
    private OnCategoryDeleteClickListener listener;

    public CardSetupCategoryLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_category_setup, this, true);

        categoryET = (EditText) findViewById(R.id.category_editText).findViewById(R.id.editText);
        cardETContainer = (LinearLayout) findViewById(R.id.cards_linearLayout);
        categoryET.setHint(getContext().getString(R.string.name_of_category));
        findViewById(R.id.addCard_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardET();
            }
        });

        findViewById(R.id.deleteButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CardSetupCategoryLayout.this.listener != null)
                    listener.onCategoryClickDelete(CardSetupCategoryLayout.this);
            }
        });

        addCardET();
    }

    private void addCardET() {
        final ViewGroup editTextLayout = (ViewGroup) LayoutInflater.from(getContext())
                .inflate(R.layout.view_card_setup_edit_text, cardETContainer, false);
        final EditText et = (EditText) editTextLayout.findViewById(R.id.editText);
        final View deleteButton = editTextLayout.findViewById(R.id.deleteButton);

        cardETs.add(et);
        cardETContainer.addView(editTextLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cardETs.remove(et);
                cardETContainer.removeView(editTextLayout);
            }
        });
    }

    public Category getCategory(Game game) {
        return new Category(categoryET.getText().toString(), getCards(game));
    }

    private Card[] getCards(Game game) {
        Card[] result = new Card[cardETs.size()];
        for (int i = 0; i < cardETs.size(); i++) {
            result[i] = new Card(cardETs.get(i).getText().toString(), game);
        }
        return result;
    }

    public void setOnCategoryDeleteClickListener(OnCategoryDeleteClickListener listener) {
        this.listener = listener;
    }

    interface OnCategoryDeleteClickListener {
        void onCategoryClickDelete(CardSetupCategoryLayout view);
    }
}