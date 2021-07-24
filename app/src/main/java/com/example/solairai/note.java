package com.example.solairai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class note extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText editText = findViewById(R.id.editext);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.solairai", Context.MODE_PRIVATE);


        int noteId;
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId",-1);
        //sending item index

        editText.setText(notesReader.notes.get(noteId));
       //setting text = notes list value at particular index

        editText.addTextChangedListener(new TextWatcher() {
            //this function will excute when we edit the text
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notesReader.notes.set(noteId, String.valueOf(charSequence));
                //setting up noteslist item value=edited text
                notesReader.arrayAdapter.notifyDataSetChanged();
                //updating list view

                HashSet<String> set = new HashSet<>(notesReader.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
                //saving changes
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
