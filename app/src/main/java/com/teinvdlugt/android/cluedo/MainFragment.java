package com.teinvdlugt.android.cluedo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainFragment extends Fragment implements CategoriesRecyclerAdapter.OnCardClickListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView categoriesRV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_main, container, false);

        categoriesRV = (RecyclerView) theView.findViewById(R.id.categories_recyclerView);
        categoriesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRV.setAdapter(new CategoriesRecyclerAdapter(getActivity(), this));

        theView.findViewById(R.id.nextTurnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickNextTurn();
                }
            }
        });

        return theView;
    }

    @Override
    public void onClick(Card card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(card.getName());

        StringBuilder msg = new StringBuilder();
        if (card.isOwned()) msg.append("Owned by ").append(card.owner().getName());
        else if (card.isPrime()) msg.append("This card is prime suspect");
        else {
            for (Player player : card.dontOwn()) {
                msg.append(player.getName()).append(" doesn't have it\n");
            }
        }

        builder.setMessage(msg);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onClickNextTurn();
    }

}

class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {

    private Context context;
    private OnCardClickListener listener;

    public CategoriesRecyclerAdapter(Context context, OnCardClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    interface OnCardClickListener {
        void onClick(Card card);
    }

    @Override
    public CategoriesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriesRecyclerAdapter.ViewHolder holder, int position) {
        Category cat = MainActivity.game.categories.get(position);
        holder.title.setText(cat.getName());

        holder.cards.removeAllViews();
        for (final Card card : cat.getCards()) {
            TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.text_view_card, holder.itemView, false);
            holder.cards.addView(tv);
            tv.setText(card.getName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onClick(card);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return MainActivity.game.categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup itemView;
        TextView title;
        LinearLayout cards;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = (ViewGroup) itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            cards = (LinearLayout) itemView.findViewById(R.id.cards_linearLayout);
        }
    }
}
