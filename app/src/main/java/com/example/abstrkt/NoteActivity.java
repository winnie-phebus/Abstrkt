package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private TextView title, noteAbstract, createdOn, lastUpdated, noteBody;
    private List<String> tags;
    private ImageButton close;
    private Note note;

    // private Note note;


    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent() != null && getIntent().getExtras() != null) {
            note = getIntent().getParcelableExtra(HomePage.NOTE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: these should all be editTexts instead of Textviews
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if (getIntent() != null && getIntent().getExtras() != null) {
            note = getIntent().getParcelableExtra(HomePage.NOTE);
        }

        title = findViewById(R.id.textView_title);
        noteAbstract = findViewById(R.id.textView_abstract);
        createdOn = findViewById(R.id.textView_createdOn);
        lastUpdated = findViewById(R.id.textView_updatedOn);
        noteBody = findViewById(R.id.textView_note);
        close = findViewById(R.id.imageButton_closeNote);

        title.setText(note.getTitle());
        noteAbstract.setText(note.getAbstract());
        createdOn.setText(formatDate(note.getCreatedOn()));
        lastUpdated.setText(formatDate(note.getUpdatedOn()));
        noteBody.setText(note.getBody());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO before sending back home, save changes
                Intent toHome = new Intent(NoteActivity.this, HomePage.class);
                startActivity(toHome);
            }
        });
    }

    private String formatDate(Date date) {
        // TODO: make this look nice??
        return date.toString();
    }


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void updateNoteFB(){

    }
}