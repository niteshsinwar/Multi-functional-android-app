package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class weather extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), featureActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }



    EditText editText;
    TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here

    editText = findViewById(R.id.editText);
    resultTextView = findViewById(R.id.resultTextView);
    }










    public void getWeather(View view) {
        try {
            DownloadTask task = new DownloadTask();
            //using object of class mentioned below
            String encodedCityName = editText.getText().toString();
             //getting name of city submitted in edit text
         task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ encodedCityName +"&appid=e177740459b02834e7c5f0632c925cf5");
           //constructing a url and excuting a task related to "task" object

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
                   }
    }//this function will setup url and show the current weather situation






    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } 
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//this function will used to download data from website




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                //picking desired feild weather info and converting into string
                Log.i("weather",weatherInfo);
                String message = "";
                JSONArray arr = new JSONArray(weatherInfo);
                //json array
                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals("")) {
                        message += main + ": " + description + "\r\n";
                    //this task will setting up the message that we want to show
                    }
                }
                //this function will iterate over all items of main and description object

              
                if (!message.equals("")) {
                    resultTextView.setText(message);
                    //this task will set resultTextView to desired value
                }
                

            }


            catch (Exception e) {
               e.printStackTrace();
            }

        }//this function will used to manipulate the data in favourable way

    }
}