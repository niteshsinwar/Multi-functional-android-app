package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class memorable extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }



    static ArrayList<String> places = new ArrayList<String>();//arraylist  to save places
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();//arraylist to save location
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorable);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here







        //accessing saved data
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.solairai", Context.MODE_PRIVATE);
        ArrayList<String> lat=new ArrayList<>();
        ArrayList<String> lon=new ArrayList<>();


        //clearing initial data
        places.clear();
        lat.clear();
        lon.clear();
        locations.clear();


        try {//adding information from saved data
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            lat = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lat",ObjectSerializer.serialize(new ArrayList<String>())));
            lon = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lon",ObjectSerializer.serialize(new ArrayList<String>())));

        }
        catch ( Exception e)
        { e.printStackTrace(); }



        //checking if it is previosly filled or not
        if (places.size()>0&&lat.size()>0&&lon.size()>0){
            if (places.size()==lat.size()&&places.size()==lon.size()){
                for (int i=0;i<lat.size();i++){
                    locations.add(new LatLng(Double.parseDouble(lat.get(i)),Double.parseDouble(lon.get(i)) ));
                }
            }
        }
        else{places.add("Add a new places...");
            locations.add(new LatLng(0,0));
        }




        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        //starting new activity and sending index of clicked item
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber",i);
                startActivity(intent);
            }
        });
    }
}