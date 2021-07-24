package com.example.solairai;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.solairai.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    //setting up modules
    LocationManager locationManager;
    LocationListener locationListener;
    private GoogleMap mMap;




    public void centerMapOnLocation(Location location, String title) {///used to showing the location

        if (location != null) {//if location is not empty
            mMap.clear();//deleting previous data
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());//getting user location
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));//show marker on user location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));//facility to move camera(Zoom)
        }
    }




    @Override//checking if permission is assigned or not for accessing Location
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            }
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }





    @Override//when map become ready then this fun will excecute
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);//setting up response on Long click!

        Intent intent = getIntent();//object is crated so that main activity can access this data
        if (intent.getIntExtra("placeNumber",0) == 0) //checking up if first item is pressed or not
        {
            // Zoom in on user location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//important steps
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(location, "Your Location");
                }
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }
                @Override
                public void onProviderEnabled(String s) {
                }
                @Override
                public void onProviderDisabled(String s) {
                }
            };

            ///checking up if permission is granted or not
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");//performing important steps if granted
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                //if not granted then it will request for permission
            }
        }



        else {//if it is not empty then
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);

            //setting up longitude and latitude of saved location to show
            placeLocation.setLatitude(memorable.locations.get(intent.getIntExtra("placeNumber",0)).latitude);
            placeLocation.setLongitude(memorable.locations.get(intent.getIntExtra("placeNumber",0)).longitude);
            centerMapOnLocation(placeLocation, memorable.places.get(intent.getIntExtra("placeNumber",0)));
        }
    }







    @Override//on long click on map
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        //transforming LONG/LAT info into street address and vice versa.
        String address = "";
        try {
            //adding all the data in address array
            List<Address> listAdddresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (listAdddresses != null && listAdddresses.size() > 0) {
                if (listAdddresses.get(0).getThoroughfare() != null) {
                    if (listAdddresses.get(0).getSubThoroughfare() != null) {
                        address += listAdddresses.get(0).getSubThoroughfare() + " "; }
                    address += listAdddresses.get(0).getThoroughfare(); } }
        }
        catch (Exception e)
        { e.printStackTrace(); }



        if (address.equals("")) //if address is not given
        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            address += sdf.format(new Date());
            //then simply add dates in address array
        }


        mMap.addMarker(new MarkerOptions().position(latLng).title(address));//while adding a marker it will capture
        //address and location
        memorable.places.add(address);//adding address in array of main activity
        memorable.locations.add(latLng);//adding coordinates in array of main activity
        memorable.arrayAdapter.notifyDataSetChanged();//when data will changed  it will notify to array adaptor


        //data storage concept used (intro)----used for saving the data
        SharedPreferences sharedPreferences =this.getSharedPreferences("com.example.solairai",Context.MODE_PRIVATE);
        try {
            ArrayList<String> lat=new ArrayList<>();
            ArrayList<String> lon=new ArrayList<>();

            for (LatLng coord : memorable.locations){
                lat.add(Double.toString(coord.latitude));
                lon.add(Double.toString(coord.longitude));
            }//saving locations to lat and lon arraylist

            sharedPreferences.edit().putString("places",ObjectSerializer.serialize(memorable.places)).apply();
            sharedPreferences.edit().putString("lats",ObjectSerializer.serialize(lat)).apply();
            sharedPreferences.edit().putString("lons",ObjectSerializer.serialize(lon)).apply();
            //saving latitude,longitude and place array to shared prefrence (object serializer is speical case function used for saving location data)
        }
        catch (IOException e)
        { e.printStackTrace(); }

        Toast.makeText(this,"Location Saved!",Toast.LENGTH_SHORT).show();
    }
}
