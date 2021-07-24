package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbershapesActivity extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), LogicalActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }






    //////////////////////////////////////////////////////EGG TIMER--1\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    Button btn;
    TextView txtview;
    String msg;
    SeekBar SeekBar2;
    SeekBar timesTablesSeekBar;
    MediaPlayer mp3;
    CountDownTimer timer;

    boolean active=false;//determining state:-as initially it is inactive
    public void click (View view) {
        //this function will excecute when we press button

        if (active) {//if timer is found in active state then clicking stop button this will happen

            SeekBar2.setProgress(30);//progress will set to 30
            SeekBar2.setEnabled(true);//seekbar will unlocked
            timesTablesSeekBar.setEnabled(false);//timeTable seekbar will locked
            active=false;//state will change from active to inactive
            txtview.setText("0:30");//text set to 30
            btn.setText("go!");
            timer.cancel();//timer will cancelled
            mp3.stop();//song will be stopped
        } //after clicking button if state found to be active then these function will excecute

        else
        {//if timer will found inactive then by clicking go button this will happen
            active = true;//state wil toggled
            SeekBar2.setEnabled(false);//user control will disabled
            timesTablesSeekBar.setEnabled(true);//timeTable seekbar will unlocked
            btn.setText("stop!");


            timer = new CountDownTimer(SeekBar2.getProgress() * 1000 + 100, 1000) {
                //setting up total time lapse         //setting up interval
                //equal to seekbar progress
                @Override
                public void onTick(long l) {
                    updatetime((int) l / 1000);
                }
                //function inside this method will perform at every interval

                @Override
                public void onFinish() {//after finishing time
                    mp3 = MediaPlayer.create(getApplicationContext(), R.raw.tune);
                    mp3.start();//playing tune
                    SeekBar2.setProgress(30);
                    SeekBar2.setEnabled(true);
                    active=false;
                    txtview.setText("0:30");
                    btn.setText("Go!");
                    timer.cancel();

                } //after ending the timer all values will set to default state
            }.start();//important function

        }
    }


    public void updatetime( int j){

        int min =j/60;
        int sec=j-(min/60);
        txtview.setText(Integer.toString(min)+":"+Integer.toString(sec));

    }
//setting up text view of timer at every interval






    //////////////////////////////////////////////////////TIME TABLE--2\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    class Number {
        int number;
        public boolean isSquare() {
            double squareRoot = Math.sqrt(number);

            if (squareRoot == Math.floor(squareRoot)) {
                //if double value of squareRoot will equal to floor value of squareRoot then it means number is a perfect square
                return true;
            }
            else {
                return false;
            }
        }

        public boolean isTriangular() {

            int x = 1;
            int triangularNumber = 1;
            while (triangularNumber < number) {

                x++;
                triangularNumber = triangularNumber + x;
            }

            if (triangularNumber == number) {
                return true;
            }
            else {
                return false;
            }
        }
    }




    ListView timesTablesListView;

    public void generateTimesTable(int timesTableNumber) {
        ArrayList<String> timesTableContent = new ArrayList<String>();

        Number mine= new Number();
        //creating an object


        for (int j = 1; j <= 10; j++) {
            int num=j * timesTableNumber;
            mine.number=num;
            //initializing value of number by user input
            msg=Integer.toString(num);
            //declare msg string with user input
            if(mine.isSquare() && mine.isTriangular()) msg+= " is both of these";
            else if(mine.isSquare()) msg+=" is square number";
            else if(mine.isTriangular()) msg+= " is triangle number";
            else msg+=" is none of these";

            timesTableContent.add(msg);
        }//creating an array

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,timesTableContent);
        timesTablesListView.setAdapter(arrayAdapter);
        //setting up array for list view

    }




    public void testNum(View view){//setting up timetableNumber and seekbar according to userinput

        EditText num=(EditText)findViewById(R.id.value);
        int n=Integer.parseInt(num.getText().toString());
        if(active==true&&(n<=10&&n>=1)){
            generateTimesTable(n);
            timesTablesSeekBar.setProgress(n);
        }
        num.setText(null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbershapes);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here






        btn=findViewById(R.id.button);
        txtview =findViewById(R.id.textView);
        SeekBar2 = findViewById(R.id.seekBar2);

        SeekBar2.setMax(600);
        SeekBar2.setProgress(30);

        SeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updatetime(i);//it will update the time as seekbar progress will changed
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //seekbar for timer





        timesTablesSeekBar = findViewById(R.id.seekBar);
        timesTablesListView = findViewById(R.id.listview);

        int max = 10;
        int startingPosition = 1;

        timesTablesSeekBar.setMax(max);
        timesTablesSeekBar.setProgress(startingPosition);
        generateTimesTable(startingPosition);//setting up initial value of timeTableNumber

        if(!active){timesTablesSeekBar.setEnabled(false);}
        if(active){timesTablesSeekBar.setEnabled(true);}

        timesTablesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int timesTableNumber;

                if (i < 1) {
                    timesTableNumber = 1;
                    timesTablesSeekBar.setProgress(1);
                }
                else {
                    timesTableNumber = i;
                }
                // timeTableNumber will change according to user input on scrollbar

                generateTimesTable(timesTableNumber);
                //passing timeTableNumber value to arrays(listview)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });//seekbar for table

    }
}