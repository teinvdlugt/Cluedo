package com.teinvdlugt.android.cluedo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        final View root = findViewById(R.id.root);

        // Limit the width to a certain width, for tablets
        root.post(new Runnable() {
            @Override
            public void run() {
                int maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
                if (root.getWidth() > maxWidth) {
                    int leftRight = (root.getWidth() - maxWidth) / 2;
                    int top = root.getPaddingTop();
                    int bottom = root.getPaddingBottom();
                    root.setPadding(leftRight, top, leftRight, bottom);
                }
            }
        });
    }

    public void onClickStart(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
