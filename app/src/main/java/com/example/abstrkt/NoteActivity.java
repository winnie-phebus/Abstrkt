package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private EditText title, noteAbstract, noteBody;

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
        RecyclerView editTags = findViewById(R.id.note_editTags);

        TextView createdOn = findViewById(R.id.textView_createdOn);
        TextView lastUpdated = findViewById(R.id.textView_updatedOn);
        ImageButton close = findViewById(R.id.imageButton_closeNote);

        title.setText(note.getTitle());
        noteAbstract.setText(note.getAbstract());
        createdOn.setText(Utils.formatDate(note.getCreatedOn()));
        lastUpdated.setText(Utils.formatDate(note.getUpdatedOn()));
        noteBody.setText(note.getBody());

        close.setOnClickListener(v -> {
            updateNoteFB();
            Intent toHome = new Intent(NoteActivity.this, HomePage.class);
            startActivity(toHome);
        });
    }

    // checks what part of the note has been updated by user, deletes basic notes
    private boolean noteUpdate() {
        boolean newTitle = isChanged(note.getTitle(), title.getText().toString());
        boolean newAbstract = isChanged(note.getAbstract(), noteAbstract.getText().toString());
        boolean newBody = isChanged(note.getBody(), noteBody.getText().toString());

        boolean noUpdate = !(newTitle || newAbstract || newBody);

        if (noUpdate && (note.getStatus().equals(Utils.N_BLANK))) { // deletes the empty note case
            note.setStatus(Utils.N_TRASH);
            Utils.deleteNote(this, note.getId());
            return false;
        }

        if (newTitle) {
            note.setTitle(title.getText().toString());
        }

        if (newAbstract) {
            note.setAbstract(noteAbstract.getText().toString());
        }

        if (newBody) {
            note.setBody(noteBody.getText().toString());
        }

        if (isChanged(note.getStatus(), Utils.N_SAVED)) {
            note.setStatus(Utils.N_SAVED);
        }

        note.updateNote();
        return true;
    }

    // simple String comparison, used to only update things as needed
    private boolean isChanged(String og, String proposed) {
        return !og.equals(proposed);
    }

    // Updating the Note in Firebase Firestore
    public void updateNoteFB() {
        noteUpdate();
        Log.d("NOTE ACTIVITY", note.getId());
        FirebaseFirestore.getInstance()
                .document(note.getId())
                .set(note)
                .addOnFailureListener(e -> {
                    Utils.toast(NoteActivity.this, "saving note failed!");
                    // TODO: make a Note backup system so that a revert local note system can be enacted in this case
                });
    }
}