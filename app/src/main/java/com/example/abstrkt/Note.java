package com.example.abstrkt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Note extends AppCompatActivity {

    private TextView title, noteAbstract, createdOn, lastUpdated, noteBody;
    private ImageButton close;

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = findViewById(R.id.textView_title);
        noteAbstract = findViewById(R.id.textView_abstract);
        createdOn = findViewById(R.id.textView_createdOn);
        lastUpdated = findViewById(R.id.textView_updatedOn);
        noteBody = findViewById(R.id.textView_note);
        close = findViewById(R.id.imageButton_closeNote);


        //TODO create a note class and have it hold these
//        title.setText(note.getTitle());
//        noteAbstract.setText(note.getAbstract());
//        createdOn.setText(note.getCreatedOnString());
//        lastUpdated.setText(note.getUpdatedOnString());
//        noteBody.setText(note.getNote());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO before sending back home, save changes
                Intent toHome = new Intent(Note.this, HomePage.class);
                startActivity(toHome);
            }
        });
    }
}