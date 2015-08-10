package com.teinvdlugt.android.cluedo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Activities that contain this fragment must implement the
 * {@link ShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShowFragment extends Fragment implements ShowRecyclerAdapter.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<Player> hadNothing = new ArrayList<>();
    private Player showed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_show, container, false);

        recyclerView = (RecyclerView) theView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ShowRecyclerAdapter(getActivity(),
                MainActivity.game.nextPlayerClockwise(MainActivity.game.getPlayerAtTurn()), this));

        return theView;
    }

    @Override
    public void onClickYes(Player player) {
        showed = player;
        // return result to MainActivity
    }

    @Override
    public void onClickNo(Player player) {
        hadNothing.add(player);

        if (MainActivity.game.nextPlayerClockwise(player).equals(MainActivity.game.getPlayerAtTurn())) {
            showed = null;
            // return result to MainActivity
        } else {
            ((ShowRecyclerAdapter) recyclerView.getAdapter()).addPlayerToList(
                    MainActivity.game.nextPlayerClockwise(player));
        }
    }

    public void returnToActivity() {
        if (mListener != null) {
            Player[] hadNothing1 = new Player[hadNothing.size()];
            for (int i = 0; i < hadNothing.size(); i++) {
                hadNothing1[i] = hadNothing.get(i);
            }
            mListener.onCardShowed(hadNothing1, showed);
        }
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
        void onCardShowed(Player[] hadNothing, Player showed);
    }
}

class ShowRecyclerAdapter extends RecyclerView.Adapter<ShowRecyclerAdapter.ShowViewHolder> {
    private ArrayList<Player> data = new ArrayList<>();
    private OnClickListener mListener;
    private Context context;

    public ShowRecyclerAdapter(Context context, Player firstToShow, OnClickListener listener) {
        this.context = context;
        this.mListener = listener;
        data.add(firstToShow);
    }

    public void addPlayerToList(Player player) {
        data.add(player);
        //notifyItemInserted(data.size()); // TODO: 9-8-2015 For animations, but buttons don't get disabled
        notifyDataSetChanged();
    }

    interface OnClickListener {
        void onClickYes(Player player);

        void onClickNo(Player player);
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_show, parent, false);
        return new ShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShowViewHolder holder, final int i) {
        holder.title.setText("Did " + data.get(i).getName() + " show something?");

        if (i == data.size() - 1) {
            holder.noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.onClickNo(data.get(i));
                }
            });
            holder.yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.onClickYes(data.get(i));
                }
            });
        } else {
            // Disable buttons
            holder.noButton.setEnabled(false);
            holder.yesButton.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button noButton, yesButton;

        public ShowViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            noButton = (Button) itemView.findViewById(R.id.noButton);
            yesButton = (Button) itemView.findViewById(R.id.yesButton);
        }
    }
}
