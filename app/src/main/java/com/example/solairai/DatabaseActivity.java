package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);



        ListView listView = (ListView) findViewById(R.id.listView);
        //defining list view
        final ArrayList<String> apps = new ArrayList<String>();
        //defining arraylist

        apps.add("Memorable places");
        apps.add("notes reader");
        apps.add("news reader");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, apps);
        //attaching adaptor view to arraylist

        listView.setAdapter(arrayAdapter);
       //connecting listview to arraylist via adaptor view

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //function that will run after when we click on list
                if(i==0) {
                    Intent intent = new Intent(getApplicationContext(), memorable.class);
                    intent.putExtra("name", apps.get(i));
                    startActivity(intent);
                }
                if(i==1) {
                    Intent intent = new Intent(getApplicationContext(), notesReader.class);
                    intent.putExtra("name", apps.get(i));
                    startActivity(intent);
                }

                if(i==2) {
                    Intent intent = new Intent(getApplicationContext(),newsReader.class);
                    intent.putExtra("name", apps.get(i));
                    startActivity(intent);
                }
            }

        });
    }
}