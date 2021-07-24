package com.example.solairai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class hiker extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), featureActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }


    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiker);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here




        //getting device location
        locationManager =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
                //this function will update the location whenever it will be changed
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null){
                updateLocationInfo(lastKnownLocation);
                //updating last known location
            };
        }
    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }//this function will excuted for granting location permission








    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }//if permission will accepted then location data will be downloaded here







    public void updateLocationInfo(Location location){

        TextView addt =findViewById(R.id.add);
        TextView latt =findViewById(R.id.lat);
        TextView lont =findViewById(R.id.lon);
        TextView altt =findViewById(R.id.alt);
        TextView velt =findViewById(R.id.vel);

        latt.setText("Latitude: "+Double.toString(location.getLatitude()));
        lont.setText("Longitude: "+Double.toString(location.getLongitude()));
        velt.setText("velocity: "+Double.toString(location.getSpeed()));
        altt.setText("Altitude: "+Double.toString(location.getAltitude()));
      // above function are used to setup location parameters




        String address = "not found :(";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){
                address="Address:\n";
                if (addressList.get(0).getThoroughfare()!=null){

                    address+=addressList.get(0).getLocality()+"\n";
                }
                if (addressList.get(0).getPostalCode()!=null){

                    address+=addressList.get(0).getAdminArea()+"\n";
                }
            }//this function will used to fetch the address data from server
        } catch (IOException e) {
            e.printStackTrace();
        }
        addt.setText(address);
    }//this function will manipulate and show  the data  according to our desire
}