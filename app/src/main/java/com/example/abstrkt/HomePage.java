package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, Utils.ActivityInterp{
    public static final String TAG = "ABSTRACT_HOME";
    public static final String NOTE = "NOTE_EXTRA";

    FirebaseUser user;

    private DrawerLayout drawerLayout;
    private EditText searchField;
    private ImageButton menuButton;

    private FragmentContainerView fragView;
    private Menu nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        user = FirebaseAuth.getInstance().getCurrentUser();

        drawerLayout = findViewById(R.id.home_menu_drawer_layout);
        menuButton = findViewById(R.id.imageButton_Menu);

        NavigationView nav = findViewById(R.id.hp_nav_view);
        nav.setNavigationItemSelectedListener(this);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();

            }
        });

        searchField = findViewById(R.id.editText_Search);

        fragView = findViewById(R.id.hp_frag_container);
    }

    public void mItemOpens(String title, Fragment dest) {
        Log.d(TAG, "mItemOpens: " + title);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(title)
                .replace(R.id.hp_frag_container, dest)
                .commit();
    }

    // opens the NoteActivity with the selected note
    public void openNoteActivity(Note curr) {
        Intent toNote = new Intent(this, NoteActivity.class);
        toNote.putExtra(NOTE, curr);
        startActivity(toNote);
    }

    @Override
    public void openTagFragment(String currentTag) {
        // TagsFragment specificTag = TagsFragment.newInstance();
        //specificTag.showTagNotes(currentTag);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("tag")
                .replace(R.id.hp_frag_container, TagsFragment.newSpecificInstance(currentTag))
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");
        int id = item.getItemId();

        if (id == R.id.nav_notes){
            mItemOpens("Notes", NotesFragment.newInstance());
            return true;
        } else if (id == R.id.nav_tags){
            mItemOpens("Tags", TagsFragment.newInstance());
            return true;
        }

        switch (id) {
            case R.id.nav_notes:
                mItemOpens("Notes", NotesFragment.newInstance());
                return true;
            case R.id.nav_tags:
                mItemOpens("Tags", TagsFragment.newInstance());
                return true;
            case R.id.nav_rules:
                mItemOpens("Rules", RulesFragment.newInstance());
                return true;
            case R.id.nav_archive:
                mItemOpens("Archive", ArchiveFragment.newInstance());
                return true;
            case R.id.nav_trash:
                mItemOpens("Trash", TrashFragment.newInstance());
                return true;
            case R.id.nav_settings:
                mItemOpens("Setting", NotesFragment.newInstance()); //TODO: make Settings a Fragment
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
