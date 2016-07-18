package com.teinvdlugt.android.cluedo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Limit the width to a certain value, for tablets
        View root = findViewById(R.id.root);
        setMaxWidth((ViewGroup) root, 600);
    }

    /**
     * Change the available width of the children of the viewGroup, if they exceed the maximum width.
     * Use for optimising layouts for tablets.
     * The available width for the children is changed by adapting the padding of the viewGroup.
     *
     * @param viewGroup The viewGroup to change the padding of.
     * @param dp        The maximum width
     */
    public static void setMaxWidth(final ViewGroup viewGroup, final int dp) {
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                int maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, viewGroup.getResources().getDisplayMetrics());
                if (viewGroup.getWidth() > maxWidth) {
                    int leftRight = (viewGroup.getWidth() - maxWidth) / 2;
                    int top = viewGroup.getPaddingTop();
                    int bottom = viewGroup.getPaddingBottom();
                    viewGroup.setPadding(leftRight, top, leftRight, bottom);
                }
            }
        });
    }

    public void onClickStart(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
