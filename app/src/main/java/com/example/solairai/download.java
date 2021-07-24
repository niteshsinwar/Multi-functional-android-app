package com.example.solairai;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class download extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), featureActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

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

            } catch (Exception e) {
                e.printStackTrace();

                return "Failed";
            }
        }
    }//this class is helpful for downloading HTML from given url



    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void Dweb(View view){
        DownloadTask task = new DownloadTask();//object of the class mentioned above
        String result = null;
        try {
            result = task.execute("https://www.zappycode.com").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(result);
        Log.i("Result",result);

    }//this function is used for setting up url and for showing up data






    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream in = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }//it is used for downloading images from given url


    ImageView imageView;
    public void downloadImage(View view) {
        ImageDownloader task = new ImageDownloader();
        Bitmap myImage;
        try {
            myImage = task.execute("https://upload.wikimedia.org/wikipedia/en/0/02/Homer_Simpson_2006.png").get();

            imageView.setImageBitmap(myImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// this function is used for setting up url for image file and showing the image


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here


        imageView = findViewById(R.id.imageView);
    }
}