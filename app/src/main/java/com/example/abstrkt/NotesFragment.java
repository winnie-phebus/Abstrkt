package com.example.abstrkt;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.text.BreakIterator;
import java.util.ArrayList;

public class NotesFragment extends Fragment {
    public static final String TAG = "ABSTRACT_NOTES";
    protected int openFolder = -1;
    private RecyclerView allFolders, folderNotes, allNotes;
    private FloatingActionButton addNew;
    private FirebaseUser user;
    private ActivityInterp interp;
    private String ownerName;
    private TextView noteText;
    private FirestoreRecyclerAdapter<Note, NoteHolder> adapter;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ownerName = user.getDisplayName();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        interp = (ActivityInterp) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        noteText = v.findViewById(R.id.textView_notes);
        allFolders = v.findViewById(R.id.recyclerView_allFolders);
        allNotes = v.findViewById(R.id.recyclerView_allNotes);
        addNew = v.findViewById(R.id.hp_new_note_fab);

        loadFolders();

        loadNotes();

        FirebaseFirestore.getInstance()
                .collection(Utils.FSN_COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //loadNotes();
                        adapter.notifyDataSetChanged();
                    }
                });

        addNew.setOnClickListener(openNote(new Note(user.getDisplayName(), new ArrayList<String>())));
        return v;
    }

    private void loadFolders() {
        Log.d(TAG, "loadFolders: ");
        FirestoreRecyclerOptions<Folder> folders = newFoldersOption();

        FirestoreRecyclerAdapter<Folder, FolderHolder> adapter =
                new FirestoreRecyclerAdapter<Folder, FolderHolder>(folders) {
  /*                  protected void forceDefer(@NonNull FolderHolder holder, int pos, String name){
                        checkStatus(pos, name);
                        toggleIcon(holder, pos);
                    }*/

                    public void toggleIcon(@NonNull FolderHolder holder, int pos) {
                        if (isOpenFolder(pos)) {
                            holder.getFolderIcon().setImageResource(R.drawable.folder_open_black);
                        } else {
                            holder.getFolderIcon().setImageResource(R.drawable.folder_closed_black);
                        }
                    }

                    private boolean isOpenFolder(int pos){
                        return openFolder == pos;
                    }

                    private void checkStatus(@NonNull FolderHolder holder, int pos, String name){
                        if (isOpenFolder(pos)) { // folder is open and should close
                            Log.d(TAG, "onClick: folder should close.");
                            openFolder = -1;
                            loadNotes();
                        }else { // for case openFolder is empty and someone else
/*                            if (openFolder != -1){
                                this.getItemViewType(pos);
                                forceDefer(holder, pos, name);
                            }*/
                            openFolder = pos;
                            updateAdapter(
                                    "All Notes in Folder: " + name,
                                    Utils.buildFieldQuery(ownerName, Utils.FSN_FOLDER, name));
                        }
                        toggleIcon(holder, pos);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull FolderHolder holder, int position, @NonNull Folder model) {

                        int pos = holder.getAbsoluteAdapterPosition();
                        holder.getFolderName().setText(model.getName());
                        holder.getLayout().setOnClickListener(new View.OnClickListener() {
                            boolean isOpen = false;

                            @Override
                            public void onClick(View view) {
                                checkStatus(holder, pos, model.getName());
                                toggleIcon(holder, pos);
                            }
                        });
                    }

                    @Override
                    public void startListening() {
                        super.startListening();
                    }

                    @NonNull
                    @Override
                    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.preview_folder, parent, false);
                        return new FolderHolder(view);
                    }
                };

        LinearLayoutManager llh = new LinearLayoutManager(getContext());
        llh.setOrientation(RecyclerView.HORIZONTAL);

        allFolders.setLayoutManager(llh);
        allFolders.setAdapter(adapter);
    }

    private FirestoreRecyclerOptions<Folder> newFoldersOption() {
        Log.d(TAG, "newFoldersOption: ");
        return new FirestoreRecyclerOptions.Builder<Folder>()
                .setQuery(Utils.buildCollectionQuery(ownerName, Utils.FSF_COLLECTION), Folder.class)
                .setLifecycleOwner(this)
                .build();
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
                                interp.openNoteActivity(curr);
                            }
                        });
            }
        };
    }

    // builds the adapter with the given query
    private void updateAdapter(String title, Query query) {
        noteText.setText(title);
        adapter = Utils.buildNoteAdapter(getContext(), query);

        allNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        allNotes.setAdapter(adapter);
    }

    // loads all the notes in Firestore of the status 'saved', regardless of tags or folders
    private void loadNotes() {
        Log.d(TAG, "loadNotes: called.");

        Query query = Utils.buildStatusQuery(ownerName, Utils.N_SAVED);
        updateAdapter(ownerName + "'s Notes", query);
    }

    // changes the Folder the given note belongs to
    private void addNoteToFolder(Note child, Folder parent) {
        // parent.addChild(Utils.docRefFromStr(child.getId()));
        child.setFolder(parent.getName());
    }

    // changes adds a new Tag for the given note
    private void addNewTagToNote(Note subject, String newTag) {
        subject.getTags().add(newTag);
    }

    interface ActivityInterp {
        void openNoteActivity(Note note);
    }
}