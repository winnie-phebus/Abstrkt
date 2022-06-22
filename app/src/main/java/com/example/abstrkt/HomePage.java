package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
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

public class HomePage extends AppCompatActivity implements NotesFragment.ActivityInterp {
    public static final String TAG = "ABSTRACT_HOME";
    public static final String NOTE = "NOTE_EXTRA";

    FirebaseUser user;

    private DrawerLayout drawerLayout;
    private EditText searchField;
    private ImageButton menuButton;
    private FragmentContainerView fragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        user = FirebaseAuth.getInstance().getCurrentUser();

        drawerLayout = findViewById(R.id.home_menu_drawer_layout);
        menuButton = findViewById(R.id.imageButton_Menu);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        searchField = findViewById(R.id.editText_Search);

        fragView = findViewById(R.id.hp_frag_container);
    }

    public void drawerLayoutSetUp(){
        MenuItem notes = drawerLayout.findViewById(R.id.nav_notes);
        MenuItem tags = drawerLayout.findViewById(R.id.nav_tags);
        MenuItem rules = drawerLayout.findViewById(R.id.nav_rules);
        MenuItem archive = drawerLayout.findViewById(R.id.nav_archive);
        MenuItem trash = drawerLayout.findViewById(R.id.nav_trash);
        MenuItem settings = drawerLayout.findViewById(R.id.nav_settings);
        MenuItem feedback = drawerLayout.findViewById(R.id.nav_feedback);
    }

    public View.OnClickListener mItemOpens(Fragment dest){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    // opens the NoteActivity with the selected note
    public void openNoteActivity(Note curr) {
        Intent toNote = new Intent(this, NoteActivity.class);
        toNote.putExtra(NOTE, curr);
        startActivity(toNote);
    }
}
