package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class newsReader extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }



    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> articleURL = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SQLiteDatabase articlesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);
        //all function inside main function will run at startup
        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here


        articlesDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles ( articleId INTEGER PRIMARY KEY, title VARCHAR, articleURL VARCHAR)");
         //creating database

        DownloadTask task = new DownloadTask();
        try {
            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");

        }//downloading article IDs
        catch (Exception e) {

        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("articleURL", articleURL.get(i));
                startActivity(intent);
            }//starting webView and sending html data
        });

        updateListView();
    }

    public void updateListView() {

        Cursor c = articlesDB.rawQuery("SELECT * FROM articles", null);
         //selecting all the items

        int titleIndex = c.getColumnIndex("title");
        int URLIndex = c.getColumnIndex("articleURL");
        //getting title and content index
        if (c.moveToFirst()) {
            titles.clear();
            articleURL.clear();
      //clearing previeos saved value of title and index
            do {
                titles.add(c.getString(titleIndex));
                articleURL.add(c.getString(URLIndex));
            //updating title and content array by database value
            }
            while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }//updating listView
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
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }//downloading data from given url


                JSONArray jsonArray = new JSONArray(result);
                  //converting json data to arraylist
                int numberOfItems = 40;
                if (jsonArray.length() < 40) {
                    numberOfItems = jsonArray.length();
                }
                Log.i("article id", "all ids were taken");
                Log.i("all ids are", result);


                articlesDB.execSQL("DELETE FROM articles");
                 //deleting values from database when app run after installation

                for (int i=0;i < numberOfItems; i++) {

                    String articleId = jsonArray.getString(i);
                    //String articleURL = "https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty";
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty");
                   //getting url of articls

                    urlConnection = (HttpURLConnection) url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    data = inputStreamReader.read();
                    String articleInfo = "";

                    while (data != -1) {
                        char current = (char) data;
                        articleInfo += current;
                        data = inputStreamReader.read();
                    }

                    JSONObject jsonObject = new JSONObject(articleInfo);
                    if (!jsonObject.isNull("title")&&!jsonObject.isNull("url")) {
                        String articleTitle = jsonObject.getString("title");
                        String articleURL = jsonObject.getString("url");
                         //extracting title and url of articles
                        String sql = "INSERT INTO articles (articleId, title, articleURL) VALUES (?, ?, ?)";
                        SQLiteStatement statement = articlesDB.compileStatement(sql);
                        statement.bindString(1, articleId);
                        statement.bindString(2, articleTitle);
                        statement.bindString(3, articleURL);
                        //adding article id,title,url to datvbase
                        statement.execute();

                    Log.i("article id", articleId);
                    Log.i("article title", articleTitle);
                    Log.i("article url", articleURL);
                }
                }
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
            //this will excecute when data will be downloaded 
        }
    }
}
