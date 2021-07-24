package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

public void runMedia(View view){

    Intent intent = new Intent(getApplicationContext(), MediaActivity.class);
        startActivity(intent);
}
    public void runLogical(View view){

        Intent intent = new Intent(getApplicationContext(), LogicalActivity.class);
        startActivity(intent);
    }
    public void runFeature(View view){

        Intent intent = new Intent(getApplicationContext(), featureActivity.class);
        startActivity(intent);
    }
    public void runDatabase(View view){

        Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}