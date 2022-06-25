package com.example.abstrkt;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    public static final String TAG = "ABSTRACT_NOTES";
    protected int openFolder = -1;
    private RecyclerView allFolders, allNotes;
    private FirebaseUser user;
    private String ownerName;
    private TextView noteText;
    private FirestoreRecyclerAdapter<Note, NoteHolder> adapter;
    private RecyclerView.ViewHolder currentItemViewHolder = null;

    // for Firebase class building
    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        return new NotesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        noteText = v.findViewById(R.id.textView_notes);
        allFolders = v.findViewById(R.id.recyclerView_allFolders);

        allNotes = v.findViewById(R.id.recyclerView_allNotes);

        FloatingActionButton addNew = v.findViewById(R.id.hp_new_note_fab);

        loadFolders();

        loadNotes();

        FirebaseFirestore.getInstance()
                .collection(Utils.FSN_COLLECTION)
                .addSnapshotListener((value, error) -> {
                    //loadNotes();
                    adapter.notifyDataSetChanged();
                });

        addNew.setOnClickListener(addNewClicked());
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

                    private boolean isOpenFolder(int pos) {
                        return openFolder == pos;
                    }

                    private void checkStatus(@NonNull FolderHolder holder, int pos, String name) {
                        if (isOpenFolder(pos)) { // folder is open and should close
                            Log.d(TAG, "onClick: folder should close.");
                            openFolder = -1;
                            loadNotes();
                        } else { // for case openFolder is empty and someone else
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
                        holder.getLayout().setOnClickListener(view -> {
                            checkStatus(holder, pos, model.getName());
                            toggleIcon(holder, pos);
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

    private View.OnClickListener addNewClicked() {
        // the add New Folder case pressed

        return view -> {
            PopupMenu addMenu = new PopupMenu(getContext(), view, Gravity.END);
            Menu in = addMenu.getMenu();

            MenuItem plusNote = in.add(Utils.PLUS_NOTE);
            // plusNote.setIcon(R.drawable.) TODO: add icons!
            MenuItem plusFolder = in.add(Utils.PLUS_FOLDER);

            MenuItem plusTag = in.add(Utils.PLUS_TAG);

            addMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle() == Utils.PLUS_NOTE) { // the add New Note case pressed
                    openNote(new Note(user.getDisplayName(), new ArrayList<>()));
                } else if (menuItem.getTitle() == Utils.PLUS_FOLDER) {
                    Utils.openAddDialog(null, getContext(), Utils.FSF_COLLECTION);
                } else if (menuItem.getTitle().equals(Utils.PLUS_TAG)) {
                    Utils.openAddDialog(null, getContext(), Utils.FST_COLLECTION);
                }
                return true;
            });
            addMenu.show();
        };
    }

    // opens a new Note to the viewer and also adds a new document to Firebase
    private void openNote(Note curr) {
        DocumentReference ref = FirebaseFirestore.getInstance()
                .collection(Utils.FSN_COLLECTION)
                .document();

        curr.setId(ref.getPath());

        ref.set(curr)
                .addOnSuccessListener(unused -> Utils.openNoteActivity(getContext(), curr));
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

        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onButtonClicked(int position, String command) {
                // button press
                Note note = adapter.getItem(position);

                switch (command) {
                    case Utils.COMM_TRASH:
                        changeNoteStatus(note, Utils.N_TRASH);
                        break;
                    case Utils.COMM_ADDFOLDER:
                        Log.d(TAG, "onButtonClicked: fold");
                        Utils.openAddDialog(note, getContext(), Utils.FSF_COLLECTION);
                        // addNoteToFolder(note, );
                        break;
                    case Utils.COMM_ARCHIVE:
                        changeNoteStatus(note, Utils.N_ARCHIVED);
                        break;
                    case Utils.COMM_ADDTAG:
                        Utils.openAddDialog(note, getContext(), Utils.FST_COLLECTION);
                        break;
                }
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(allNotes);
        allNotes.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }


    // update the given Note's status - TODO: might be unnecessary
    private void changeNoteStatus(Note note, String newStatus) {
        note.setStatus(newStatus);
        Utils.updateNoteFB(note);
    }


}