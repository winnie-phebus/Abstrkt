package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private EditText title, noteAbstract, noteBody;
    private TextView lastUpdated, createdOn;
    private ImageButton close;
    private RecyclerView editTags;

    private List<String> tags;
    private Note note;

    // TODO: implement 'add tag to note'

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent() != null && getIntent().getExtras() != null) {
            note = getIntent().getParcelableExtra(HomePage.NOTE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if (getIntent() != null && getIntent().getExtras() != null) {
            note = getIntent().getParcelableExtra(HomePage.NOTE);
        }

        // editable content
        title = findViewById(R.id.editText_title);
        noteAbstract = findViewById(R.id.editText_abstract);
        noteBody = findViewById(R.id.editText_note);
        editTags = findViewById(R.id.note_editTags);

        createdOn = findViewById(R.id.textView_createdOn);
        lastUpdated = findViewById(R.id.textView_updatedOn);
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
                updateNoteFB();
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

    private void noteUpdate(){
        note.setTitle(title.getText().toString());
        note.setAbstract(noteAbstract.getText().toString());
        note.setBody(noteBody.getText().toString());
        note.updateNote();
    }

    public void updateNoteFB(){
        noteUpdate();
        Log.d("NOTE ACTIVITY", note.getId());
        FirebaseFirestore.getInstance()
                .document(note.getId())
                .set(note)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoteActivity.this, "saving note failed!", Toast.LENGTH_SHORT);
                    }
                });
    }
}