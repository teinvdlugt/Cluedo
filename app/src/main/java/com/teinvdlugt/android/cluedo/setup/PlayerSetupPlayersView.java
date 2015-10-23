package com.teinvdlugt.android.cluedo.setup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teinvdlugt.android.cluedo.R;
import com.teinvdlugt.android.cluedo.io.PlayerSetupJSONUtils;

import java.util.List;


public class PlayerSetupPlayersView extends LinearLayout {
    public interface OnPlayerNameClickListener {
        void onPlayerNameClick(String name);
    }

    private OnPlayerNameClickListener mListener;

    private void init() {
        setOrientation(VERTICAL);
        loadPlayers();
    }

    private void loadPlayers() {
        removeAllViews();
        List<String> names = PlayerSetupJSONUtils.getPlayers(getContext());

        LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int padding16dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        for (final String name : names) {
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(p);
            tv.setPadding(padding16dp, padding16dp, padding16dp, padding16dp);
            tv.setTextSize(18);
            tv.setTextColor(Color.BLACK);
            tv.setText(name);

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.onPlayerNameClick(name);
                }
            });
            tv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setMessage(getContext().getString(R.string.delete_saved_player_confirmation_message, name))
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PlayerSetupJSONUtils.deletePlayer(getContext(), name);
                                    loadPlayers();
                                }
                            }).setNegativeButton(android.R.string.cancel, null)
                            .create().show();
                    return true;
                }
            });

            // Set selectableItemBackground
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray ta = getContext().obtainStyledAttributes(attrs);
            Drawable selectableItemBackgroundDrawable = ta.getDrawable(0);
            ta.recycle();
            if (Build.VERSION.SDK_INT >= 16) {
                tv.setBackground(selectableItemBackgroundDrawable);
            } else {
                tv.setBackgroundDrawable(selectableItemBackgroundDrawable);
            }

            addView(tv);
        }
    }

    public void setOnPlayerNameClickListener(OnPlayerNameClickListener listener) {
        mListener = listener;
    }

    public PlayerSetupPlayersView(Context context) {
        super(context);
        init();
    }

    public PlayerSetupPlayersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
}
