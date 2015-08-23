package com.teinvdlugt.android.cluedo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements CardsFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        ShowFragment.OnFragmentInteractionListener {

    public static Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initGame();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new MainFragment())
                .commit();
    }

    private void enterImmersiveMode() {
        // TODO: 23-8-2015 Needs to be tested on older APIs
        findViewById(R.id.fragment_container).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void initGame() {
        game = new Game();
        Category suspects = new Category("Suspects",
                new Card("Draco Malfidus", game),
                new Card("Korzel en Kwast", game),
                new Card("Lucius Malfidus", game),
                new Card("Peter Pippeling", game),
                new Card("Dorothea Omber", game),
                new Card("Bellatrix van Detta", game));
        Category weapons = new Category("Weapons",
                new Card("Slaapdrank", game),
                new Card("Viavia", game),
                new Card("Impedimenta", game),
                new Card("Verdwijnkast", game),
                new Card("Petrificus Totalus", game),
                new Card("Mandragora", game));
        Category places = new Category("Places",
                new Card("Grote zaal", game),
                new Card("Ziekenzaal", game),
                new Card("Kamer van Hoge Nood", game),
                new Card("Toverdranklokaal", game),
                new Card("Prijzenkamer", game),
                new Card("Waarzeggerijlokaal", game),
                new Card("Uilenvleugel", game),
                new Card("Bibliotheek", game),
                new Card("Verweer tegen de zwarte kunsten", game));

        game.categories.add(suspects);
        game.categories.add(weapons);
        game.categories.add(places);

        Player tein = new Player("Tein", game, 4);
        tein.setOwns(game.categories.get(0).getCards()[0]);
        tein.setOwns(game.categories.get(0).getCards()[1]);
        tein.setOwns(game.categories.get(1).getCards()[0]);
        tein.setOwns(game.categories.get(2).getCards()[0]);
        game.players.add(tein);
        game.players.add(new Player("Lucel", game, 4));
        game.players.add(new Player("Kees", game, 4));
        game.players.add(new Player("Bregt", game, 5));
        game.players.add(new Player("Saar", game, 4));

        game.setAppUser(tein);
        game.setPlayerAtTurn(game.players.get(3)); // Lucel
    }

    @Override
    public void onClickNextTurn() {
        slideFragment(new CardsFragment());
    }

    public static Card[] chosenCards;

    @Override
    public void onCardsChosen(Card... cards) {
        chosenCards = cards;
        slideFragment(new ShowFragment());
    }

    @Override
    public void onCardShowed(Player[] hadNothing, Player showed, Card cardShowed) {
        game.turn(hadNothing, showed, chosenCards, cardShowed);
        slideFragment(new MainFragment());
    }

    private void slideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_immersive) {
            enterImmersiveMode();
        }
        return super.onOptionsItemSelected(item);
    }
}
