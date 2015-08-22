package com.teinvdlugt.android.cluedo;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CardsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    TextView turnTV;
    TextView[] textViews = new TextView[3];
    Spinner[] spinners = new Spinner[3];
    Button goButton;

    ArrayList<Card> cards = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_cards, container, false);

        initViews(theView);
        setTitleText(theView);

        for (int i = 0; i < 3; i++) {
            textViews[i].setText(MainActivity.game.categories.get(i).getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, MainActivity.game.categories.get(i).cardNames());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners[i].setAdapter(adapter);
        }

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGo();
            }
        });

        return theView;
    }

    private void setTitleText(View view) {
        // TODO: 12-8-2015 Extract string resources
        if (!MainActivity.game.getPlayerAtTurn().equals(MainActivity.game.getAppUser())) {
            turnTV.setText("It is " + MainActivity.game.getPlayerAtTurn().getName() + "'s turn");
        } else {
            turnTV.setText("It\'s your turn!");
            ((TextView) view.findViewById(R.id.turn_subtitle)).setText("Who do you suspect?");
        }
    }

    private void onClickGo() {
        // Register chosen cards
        for (int i = 0; i < spinners.length; i++) {
            int selected = spinners[i].getSelectedItemPosition();
            Card card = MainActivity.game.categories.get(i).getCards()[selected];
            cards.add(card);
        }

        if (mListener != null) {
            // Convert ArrayList<Card> to Card[]
            Card[] cards1 = new Card[cards.size()];
            for (int i = 0; i < cards.size(); i++) {
                cards1[i] = cards.get(i);
            }

            mListener.onCardsChosen(cards1);
        }
    }

    private void initViews(View theView) {
        turnTV = (TextView) theView.findViewById(R.id.turn_tv);
        textViews[0] = (TextView) theView.findViewById(R.id.category1tv);
        textViews[1] = (TextView) theView.findViewById(R.id.category2tv);
        textViews[2] = (TextView) theView.findViewById(R.id.category3tv);
        spinners[0] = (Spinner) theView.findViewById(R.id.category1sp);
        spinners[1] = (Spinner) theView.findViewById(R.id.category2sp);
        spinners[2] = (Spinner) theView.findViewById(R.id.category3sp);
        goButton = (Button) theView.findViewById(R.id.buttonGo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
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
        void onCardsChosen(Card... cards);
    }
}
