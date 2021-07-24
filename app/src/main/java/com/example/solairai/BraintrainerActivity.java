package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class BraintrainerActivity extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), LogicalActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }


    ArrayList<Integer> answers=new ArrayList<>();
    ConstraintLayout layout;
    Button gobutton;
    Button agnbutton;
    Button btn0;
    Button btn1;
    Button btn2;
    Button btn3;

    TextView restxtview;
    TextView scrtxtview;
    TextView sumTextView;
    TextView tmrtextview;

    int locnOfCrctAns;
    int score=0;
    int noOfQues=0;
//declaring all the varables

    public void playagain(View view2){
        //this button will appear when we r done with atleast one match
        //this function will initialize all the states
        score=0;
        noOfQues =0;
        tmrtextview.setText("30s");
        restxtview.setText(" ");
        scrtxtview.setText(Integer.toString(score)+"/"+Integer.toString(noOfQues));
        agnbutton.setVisibility(View.INVISIBLE);
        newQuestion();
        new CountDownTimer(30100,1000){
                           //total time         //interval
            @Override
            public void onTick(long l) {
                tmrtextview.setText(String.valueOf(l/1000)+"s");
            }
                                        //setting up timer text = l/1000
                                        //where l=30000 and decreased j=j-1000 every second
            @Override
            public void onFinish() {

                restxtview.setText("done!");
                agnbutton.setVisibility(View.VISIBLE);
                //on finishing this will excecute
            }
        }.start();

    }

    public void chooseAnswer (View view1) {//when we click the option
        if (Integer.toString(locnOfCrctAns).equals(view1.getTag().toString()))
        {//increase score if answer is correct
            restxtview.setText("correct!");
            score++;
        }
        else
        {
            restxtview.setText("wrong!");
        }
        noOfQues++;//icrease number of que after evry response

        scrtxtview.setText(Integer.toString(score)+"/"+Integer.toString(noOfQues));
        //setting up scorecard
        newQuestion();
    }

    public void start(View view){
//this function will excecute when we start game for first time
        gobutton.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);
        playagain(agnbutton);
    }

    public void newQuestion(){
        //generating random numbers  (ie 34+22)
        Random rand = new Random();
        int a=   rand.nextInt(40);
        int b=   rand.nextInt(40);
        sumTextView.setText(Integer.toString(a)+" + "+Integer.toString(b));

        locnOfCrctAns = rand.nextInt(4);//setting up location of correct answer
        answers.clear();//clearing previos values

        for (int i=0;i<4;i++){
            if (i==locnOfCrctAns)
                answers.add(a+b);//inserting correct answer in correct location
            else
                answers.add( rand.nextInt(80));//inserting other answers
        }

        btn0.setText(Integer.toString(answers.get(0)));
        btn1.setText(Integer.toString(answers.get(1)));
        btn2.setText(Integer.toString(answers.get(2)));
        btn3.setText(Integer.toString(answers.get(3)));
      //setting up MCQs
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braintrainer);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here


        sumTextView=findViewById(R.id.sumTextView);
        scrtxtview=findViewById(R.id.scoreTextView);
        restxtview=findViewById(R.id.resultTextView);
        tmrtextview=findViewById(R.id.timerTextView);
        agnbutton=findViewById(R.id.agnbutton);
        gobutton=findViewById(R.id.goButton);
        btn0 =findViewById(R.id.button0);
        btn1 =findViewById(R.id.button1);
        btn2 =findViewById(R.id.button2);
        btn3 =findViewById(R.id.button3);
        layout=findViewById(R.id.game);
         //initializing varaibles
        gobutton.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);
        //setting up visibility
    }
}