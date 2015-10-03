package com.teinvdlugt.android.cluedo.setup;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teinvdlugt.android.cluedo.Card;
import com.teinvdlugt.android.cluedo.Category;
import com.teinvdlugt.android.cluedo.MainActivity;
import com.teinvdlugt.android.cluedo.R;

import java.util.ArrayList;

public class CategoryTreeView extends LinearLayout {
    private CategoryItemView[] categoryViews;

    private void init() {
        setOrientation(VERTICAL);
        categoryViews = new CategoryItemView[MainActivity.game.categories.size()];
        java.util.ArrayList<Category> categories = MainActivity.game.categories;
        for (int i = 0; i < categories.size(); i++) {
            CategoryItemView catView = new CategoryItemView(getContext(), categories.get(i));
            categoryViews[i] = catView;
            addView(catView);
        }
    }

    public ArrayList<Card> getSelectedCards() {
        ArrayList<Card> result = new ArrayList<>();
        for (CategoryItemView category : categoryViews)
            result.addAll(category.getSelectedCards());
        return result;
    }

    @SuppressLint("ViewConstructor")
    private static class CategoryItemView extends LinearLayout {
        private Category category;
        private CheckBox[] checkBoxes;
        private LinearLayout checkBoxContainer;
        private ImageButton chevron;

        private void init() {
            setOrientation(VERTICAL);

            // Add header with name of category:
            ViewGroup header = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_category_node, this, false);
            ((TextView) header.findViewById(R.id.node_textView)).setText(category.getName());
            header.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandOrCollapse();
                }
            });
            chevron = (ImageButton) header.findViewById(R.id.node_chevron_image);
            chevron.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandOrCollapse();
                }
            });
            addView(header);

            // Create container for the checkboxes
            checkBoxContainer = new LinearLayout(getContext());
            checkBoxContainer.setOrientation(VERTICAL);
            // TODO: 3-10-2015 Actually use margin instead of padding
            int _30dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            checkBoxContainer.setPadding(_30dp, 0, 0, 0);
            addView(checkBoxContainer);

            // Add card checkboxes
            Card[] cards = category.getCards();
            checkBoxes = new CheckBox[cards.length];
            for (int i = 0; i < cards.length; i++) {
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setText(cards[i].getName());
                checkBoxes[i] = checkBox;
                checkBoxContainer.addView(checkBox);
            }
        }

        private void expandOrCollapse() {
            switch (checkBoxContainer.getVisibility()) {
                case View.VISIBLE:
                    checkBoxContainer.setVisibility(View.GONE);
                    chevron.setImageResource(R.drawable.ic_chevron_right_24dp);
                    break;
                case View.GONE:
                    checkBoxContainer.setVisibility(View.VISIBLE);
                    chevron.setImageResource(R.drawable.ic_expand_more_24dp);
                    break;
            }
        }

        public ArrayList<Card> getSelectedCards() {
            ArrayList<Card> result = new ArrayList<>();
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isChecked())
                    result.add(category.getCards()[i]);
            }
            return result;
        }

        public CategoryItemView(Context context, Category category) {
            super(context);
            this.category = category;
            init();
        }
    }

    public CategoryTreeView(Context context) {
        super(context);
        init();
    }

    public CategoryTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
}
