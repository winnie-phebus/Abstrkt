package com.example.abstrkt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TagsFragment extends Fragment {
    private FirebaseUser user;
    private TextView title;
    private String tag;
    private RecyclerView tagNotes;
    private String ownerName;
    private FirestoreRecyclerAdapter<Note, NoteHolder> adapter;

    public TagsFragment() {
        // Required empty public constructor
    }

    public static TagsFragment newInstance() {
        return new TagsFragment();
    }

    public static TagsFragment newSpecificInstance(String tag){
        TagsFragment showTag = new TagsFragment();
        Bundle args = new Bundle();
        args.putString("TAG", tag);
        showTag.setArguments(args);
        return showTag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            tag = getArguments().getString("TAG");
        }
    }

    // shows all notes associated with a given tag
    public void showTagNotes(String tag) {
        title.setText("Notes in Tag: " + tag);
        FirestoreRecyclerAdapter<Note, NoteHolder> adapter = Utils.buildNoteAdapter(getContext(), Utils.buildArrQuery(ownerName, Utils.FSN_TAGS, tag));
        setRVAdapter(adapter);
    }

    public void showAllUserTags() {
        setRVAdapter(Utils.buildTagAdapter(this, getContext(), Utils.buildCollectionQuery(ownerName, Utils.FST_COLLECTION)));
    }

    // a sneaky way to get a Fragment to save a specific adapter for later
    public void saveAdapter(FirestoreRecyclerAdapter<Note, NoteHolder> adapter) {
        this.adapter = adapter;
    }

    // changes the tag display recyclerview to use the given adapter instead
    public void setRVAdapter(FirestoreRecyclerAdapter<Note, NoteHolder> adapter) {
        saveAdapter(adapter);
        if (tagNotes != null) {
            tagNotes.setAdapter(this.adapter);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ownerName = user.getDisplayName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tags, container, false);
        title = v.findViewById(R.id.tags_tags_title);
        title.setText("All Tags");
        tagNotes = v.findViewById(R.id.tags_rv);

        tagNotes.setLayoutManager(new LinearLayoutManager(getContext()));

        if (tag != null){
            showTagNotes(tag);
        } else {
            showAllUserTags();
        }
        return v;
    }
}