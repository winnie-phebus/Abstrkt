package com.example.abstrkt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

        loadFolders();
        Query userNotes = buildQuery(Utils.FSN_COLLECTION, Utils.FSN_UPDATED);
        loadNotes(userNotes);
    }

    private void loadFolders() {
    }

    private Query buildQuery(String collection, String order){
        Query query = FirebaseFirestore.getInstance()
                .collection(collection)
                .orderBy(order)
                .limit(50); // TODO: do we want this? AKA. make more customizable

        return query;
    }

    private void loadNotes(Query query) {
        // Configure recycler adapter options:
//  * query is the Query object defined above.
//  * Chat.class instructs the adapter to convert each DocumentSnapshot to a Chat object
        FirestoreRecyclerOptions<Note> notes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Note, NoteHolder>(notes) {
            @Override
            public void onBindViewHolder(NoteHolder holder, int position, Note model) {
                holder.getTitle().setText(model.getTitle());

                String summary = model.getAbstract();
                if (model.getAbstract() == null){
                    summary = model.getBody();
                }

                holder.getNoteSummary().setText(summary);
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
    }
}
