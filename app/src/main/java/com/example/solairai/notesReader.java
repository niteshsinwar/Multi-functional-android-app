package com.example.solairai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class notesReader extends AppCompatActivity {
    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
        startActivity(intent);
        //this function is used to running other activity
    }



    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_reader);
        //all function inside main function will run at startup

        Intent intent = getIntent();
        //information is gathered from previous activity
        Toast.makeText(this, intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        //and then toasted here





        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.solairai", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        //used to set up shared prefrence for storing arraylist

        if (set == null)
        {//if app run first time
            notes.add("Example Note");
        }
        else {//or if it is previosly filled then filled the notes list with saved data
            notes = new ArrayList(set);
        }


        ListView listView = findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),note.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });//it will open the notes when we click on any item



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //this function will excecute when we long click an item
                final int itemToDelete = i;

                new AlertDialog.Builder(notesReader.this)
                        //alert dialog box
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override//this function will excecutre when we click yes
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                //deleting item from notes then updating list view
                                HashSet<String> set = new HashSet<>(notesReader.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                //saving changes
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();// nothing will happen when we click no

                return true;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }//MENUBAR essentials

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add) {
            notes.add("add notes...");
            notesReader.arrayAdapter.notifyDataSetChanged();
            //add notes and then udating list view
            HashSet<String> set = new HashSet<>(notesReader.notes);
            sharedPreferences.edit().putStringSet("notes", set).apply();
            //saving changes
            return true;
        }

        return false;
    }
}