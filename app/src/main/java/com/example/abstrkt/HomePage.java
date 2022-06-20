package com.example.abstrkt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {


    private EditText searchField;
    private ImageButton menuButton;
    private RecyclerView allFolders, allNotes;
    private FloatingActionButton addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        searchField = findViewById(R.id.editText_Search);
        menuButton = findViewById(R.id.imageButton_Menu);
        allFolders = findViewById(R.id.recyclerView_allFolders);
        allNotes = findViewById(R.id.recyclerView_allNotes);
        addNew = findViewById(R.id.hp_new_note_fab);
    }
}