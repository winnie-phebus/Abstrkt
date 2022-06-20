package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    public static final String TAG = "ABSTRACT_HOME";
    public static final String NOTE = "NOTE_EXTRA";
    FirebaseUser user;
    private EditText searchField;
    private ImageButton menuButton;
    private RecyclerView allFolders, allNotes;
    private FloatingActionButton addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        user = FirebaseAuth.getInstance().getCurrentUser();

        searchField = findViewById(R.id.editText_Search);
        menuButton = findViewById(R.id.imageButton_Menu);
        allFolders = findViewById(R.id.recyclerView_allFolders);
        allNotes = findViewById(R.id.recyclerView_allNotes);
        addNew = findViewById(R.id.hp_new_note_fab);

        loadFolders();

        Query userNotes = buildQuery(Utils.FSN_COLLECTION, Utils.FSN_UPDATED);
        loadNotes(userNotes);
        FirebaseFirestore.getInstance().collection(Utils.FSN_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                updateAdapter();
            }
        });

        addNew.setOnClickListener(openNote(new Note(user.getDisplayName(), new ArrayList<String>())));
    }

    // opens a new Note to the viewer and also adds a new document to Firebase
    private View.OnClickListener openNote(Note curr) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ref = FirebaseFirestore.getInstance()
                        .collection(Utils.FSN_COLLECTION)
                        .document();

                curr.setId(ref.getPath());

                ref.set(curr)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                openNoteActivity(curr);
                            }
                        });
            }
        };
    }

    // opens the NoteActivity with the selected note
    private void openNoteActivity(Note curr) {
        Intent toNote = new Intent(this, NoteActivity.class);
        toNote.putExtra(NOTE, curr);
        startActivity(toNote);
    }

    private void loadFolders() {
    }

    private Query buildQuery(String collection, String order) {
        Query query = FirebaseFirestore.getInstance()
                .collection(collection)
                .whereEqualTo(Utils.FSN_OWNER, user.getDisplayName())
                .orderBy(order)
                .limit(50); // TODO: ++ - maybe add a filtering function?

        return query;
    }

    private FirestoreRecyclerOptions<Note> newNotesOption(Query query){
        FirestoreRecyclerOptions<Note> notes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .setLifecycleOwner(this)
                .build();

        return notes;
    }

    private void updateAdapter(){
        loadNotes(buildQuery(Utils.FSN_COLLECTION, Utils.FSN_UPDATED));
    }

    private void loadNotes(Query query) {
        Log.d(TAG, "loadNotes: called.");

        FirestoreRecyclerOptions<Note> notes = newNotesOption(query);

        FirestoreRecyclerAdapter<Note, NoteHolder> adapter =
                new FirestoreRecyclerAdapter<Note, NoteHolder>(notes) {

                    @Override
                    public void onBindViewHolder(NoteHolder holder, int position, Note model) {
                        holder.getTitle().setText(model.getTitle());

                        String summary = model.getAbstract();
                        if (model.getAbstract() == null) {
                            summary = model.getBody();
                        }

                        holder.getNoteSummary().setText(summary);

                        LinearLayoutManager llh = new LinearLayoutManager(getBaseContext());
                        llh.setOrientation(RecyclerView.HORIZONTAL);
                        holder.getTags().setLayoutManager(llh);
                        holder.getTags().setAdapter(new NoteTagAdapter(model.getTags()));

                        holder.getContainer().setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openNoteActivity(model);
                                    }
                                }
                        );
                    }

                    @NonNull
                    @Override
                    public NoteHolder onCreateViewHolder(ViewGroup group, int i) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        View view = LayoutInflater.from(group.getContext())
                                .inflate(R.layout.preview_note, group, false);

                        return new NoteHolder(view);
                    }
                };

        // FirestoreRecyclerOptions.Builder().setLifecycleOwner(...)
        allNotes.setLayoutManager(new LinearLayoutManager(this));
        allNotes.setAdapter(adapter);
    }
}
