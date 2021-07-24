package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class tictactoeActivity extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), LogicalActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }


    //if game state= 0: yellow, 1: red, 2: empty
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    // it will indicate that what is current scenerio at particular location(i.e 0 to 8)
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    //combination of winning positions

    int activePlayer = 0;
    // if activeplayer=0 then i have to fill yellow ball
    //if activeplayer =1 then i have to fill red ball




    boolean gameActive = true;
    //show that game is active or not
    public void dropIn(View view) {
        //function will activate when we click on any of the nine position
        ImageView counter = (ImageView) view;
        //it will take view(imageview)
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        //it take value of tag that we click
        if (gameState[tappedCounter] == 2 && gameActive) {

            //this will check that we clicked that on tag before or not
            gameState[tappedCounter] = activePlayer;
            //this will set the game state to yellow/red
            counter.setTranslationY(-1500);
            // initial position of markers



            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
                                   }
            else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
                 }


            counter.animate().translationYBy(1500).setDuration(300);
            // take those balls to their respective locations
            for (int[] winningPosition : winningPositions) {
                //iterate over winning position array

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    //check that we got match on winning position or not

                    gameActive = false;
                    String winner = "";

                    if (activePlayer == 1) {
                        winner = "Yellow";
                    }
                    else {
                        winner = "Red";
                        }
                    // this will check that what is our last entry

                    Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
                    TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
                    winnerTextView.setText(winner + " has won!");


                    playAgainButton.setVisibility(View.VISIBLE);
                    //playagain button will become visible
                    winnerTextView.setVisibility(View.VISIBLE);
                    //winner textview will become visible
                }
            }
        }
    }



    public void playAgain(View view) {
        //this function will ececute when we click play again button
        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        // take id value (button)
        TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);

        playAgainButton.setVisibility(View.INVISIBLE);
        //set playagain button to invisible
        winnerTextView.setVisibility(View.INVISIBLE);
        //set text view to invisible


        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        for(int i=0; i<gridLayout.getChildCount(); i++) {
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
        }
        //set all image view to empty state


        for (int i=0; i<gameState.length; i++) {
            gameState[i] = 2;
                                                }
        activePlayer = 0;
        gameActive = true;
    }//set all parameter to inital state



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here
    }
}