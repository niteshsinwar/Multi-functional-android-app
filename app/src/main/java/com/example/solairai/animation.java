package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class animation extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MediaActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }



    boolean i=true;
    // it indicate that for i=true empty box is visible
    public void fade(View view){
        //checkbox

        ImageView I=(ImageView) findViewById(R.id.img2);
        //emptybox
        ImageView j=(ImageView) findViewById(R.id.image2);
        //filledbox

        if(i)//checking its current state
        {
            i=false;
            I.animate().alpha(0).setDuration(3000);
            //make i invisible
            j.animate().alpha(1).setDuration(3000);
            //make j visible
        }
        else {
            i=true;
            I.animate().alpha(1).setDuration(3000);
            j.animate().alpha(0).setDuration(3000);
        }
    }



    boolean q=true;
    //it indicate that if q= true than arrow will at its starting position
    public void move(View view){
   //arrow
        ImageView I=(ImageView) findViewById(R.id.a);
        //setting up image view value

        if(q)//checking current state
        {
            q=false;
            I.animate().translationXBy(200).setDuration(1000);
            //move the object
        }
        else {
            q=true;
            I.animate().translationXBy(-200).setDuration(1000);
        }
    }




    boolean r=true;
    //if r=true then circle is in its original shape
    public void large(View view){
        //circle

        ImageView I=(ImageView) findViewById(R.id.red);
        //setting up image view value
        if(r) {
            r=false;
            I.animate().scaleX(2).scaleY(2).setDuration(3000);
            //change its size
        }
        else {
            r=true;
            I.animate().scaleX(1).scaleY(1).setDuration(3000);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
//all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here

        ImageView I=(ImageView) findViewById(R.id.star);
        I.animate().rotationBy(2000000).setDuration(10000);
        //animation of rotation will be occur for given duration
    }
}