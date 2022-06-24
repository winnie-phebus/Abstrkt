package com.example.abstrkt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
    private static final String TAG = "ABSTRACT_UTILS";

    // the field values for Note Documents in Firebase, for ease and consistency
    public static final String FSN_COLLECTION = "notes";
    public static final String FSN_TITLE = "title";
    public static final String FSN_ABSTRACT = "abstract";
    public static final String FSN_BODY = "body";
    public static final String FSN_CREATED = "createdOn";
    public static final String FSN_UPDATED = "updatedOn";
    public static final String FSN_TAGS = "tags";
    public static final String FSN_FOLDER = "folder";
    public static final String FSN_OWNER = "owner";
    public static final String FSN_ID = "id";
    public static final String FSN_STATUS = "status";
    // the field values for Folder documents in Firebase, for ease and consistency
    public static final String FSF_COLLECTION = "folders";
    public static final String FSF_NAME = "name";
    public static final String FSF_OWNER = "owner";
    // the field values for 'Tag' documens in Firebase, for ease and consistency
    public static final String FST_COLLECTION = "tags";
    public static final String FST_NAME = "title";
    // the different status states for a note
    public static final String N_BLANK = "BLANK";
    public static final String N_SAVED = "SAVED";
    public static final String N_ARCHIVED = "HIDDEN";
    public static final String N_TRASH = "TO DELETE";

    // UNIVERSAL / GENERALLY USEFUL METHODS:

    // returns a DocumentReference for the given path, may not be used anymore :(
    public static DocumentReference docRefFromStr(String path) {
        return FirebaseFirestore.getInstance().document(path);
    }

    // builds a query that looks for a specific collection
    public static Query buildCollectionQuery(String owner, String collection) {
        return FirebaseFirestore.getInstance()
                .collection(collection)
                .whereEqualTo(Utils.FSN_OWNER, owner)
                .limit(50); // TODO: ++ - maybe add a filtering function?
    }

    // specifically builds a Query through the notes collection for statuses
    public static Query buildStatusQuery(String owner, String desiredStatus) {
        return FirebaseFirestore.getInstance()
                .collection(Utils.FSN_COLLECTION)
                .whereEqualTo(Utils.FSN_OWNER, owner)
                .whereEqualTo(Utils.FSN_STATUS, desiredStatus);
    }

    // specifically builds a Query through the notes collection looking for specific fields
    public static Query buildFieldQuery(String owner, String field, String target){
        return buildStatusQuery(owner, N_SAVED).whereEqualTo(field, target);
    }

    // builds a Query through the Notes collection looking for a value within an arr field
    public static Query buildArrQuery(String owner, String arrField, String target){
        return buildStatusQuery(owner, N_SAVED).whereArrayContains(arrField, target);
    }

    // returns a NotesOptions object that handles the given query, which can be put into an adapter
    public static FirestoreRecyclerOptions<Note> newNotesOption(Context context, Query query) {
        FirestoreRecyclerOptions<Note> notes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .setLifecycleOwner((LifecycleOwner) context)
                .build();

        return notes;
    }

    public static FirestoreRecyclerAdapter<Note, NoteHolder> buildTagAdapter(TagsFragment frag, Context context, Query query){
        FirestoreRecyclerOptions<Note> notes = newNotesOption(context, query);

        return new FirestoreRecyclerAdapter<Note, NoteHolder>(notes) {
            @Override
            public void onBindViewHolder(NoteHolder holder, int position, Note model) {
                model = makeTag(model);
                Note finalModel = model;

                holder.getTitle().setText(model.getTitle());

                String summary = model.getAbstract();
                if (model.getAbstract() == null || TextUtils.isEmpty(model.getAbstract())) {
                    summary = model.getBody();
                }

                holder.getNoteSummary().setText(summary);

                // the 'tags' of a tag are actually its rules :)
                LinearLayoutManager llh = new LinearLayoutManager(context);
                llh.setOrientation(RecyclerView.HORIZONTAL);
                holder.getTags().setLayoutManager(llh);
                holder.getTags().setAdapter(new NoteTagAdapter(model.getTags()));


                holder.getContainer().setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                frag.showTagNotes(finalModel.getTitle());
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
    }

    // returns the adapter that is used almost universally
    // (ex: referenced by NoteFragment, TagsFragment, ArchiveFragment, and TrashFragment)
    public static FirestoreRecyclerAdapter<Note, NoteHolder> buildNoteAdapter(Context context, Query query) {
        FirestoreRecyclerOptions<Note> notes = newNotesOption(context, query);

        return new FirestoreRecyclerAdapter<Note, NoteHolder>(notes) {
            @Override
            public void onBindViewHolder(NoteHolder holder, int position, Note model) {
                holder.getTitle().setText(model.getTitle());

                String summary = model.getAbstract();
                if (model.getAbstract() == null || TextUtils.isEmpty(model.getAbstract())) {
                    summary = model.getBody();
                }

                holder.getNoteSummary().setText(summary);

                LinearLayoutManager llh = new LinearLayoutManager(context);
                llh.setOrientation(RecyclerView.HORIZONTAL);
                holder.getTags().setLayoutManager(llh);
                holder.getTags().setAdapter(new NoteTagAdapter(model.getTags()));

                holder.getContainer().setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openNoteActivity(context, model);
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
    }

    // removes the note from Firestore
    // used to not save blank notes and in trash / rules
    public static void deleteNote(Context context, String id) {
        FirebaseFirestore.getInstance().document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        toast(context, "Note successfully deleted.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        toast(context, "Error deleting requested document: " + e.getMessage());
                    }
                });
    }

    // just a shorthand for making consistent toasts
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static ActivityInterp buildInterp(Context context){
        return (ActivityInterp) context;
    }

    public static void openTagFragment(Context context, String currentTag) {
        buildInterp(context).openTagFragment(currentTag);
    }

    public static void openNoteActivity(Context context, Note curr) {
        buildInterp(context).openNoteActivity(curr);
    }

    // returns the current time
    public static Date exactTime() {
        return Timestamp.from(ZonedDateTime.now().toInstant());
    }

    // formats a Note so that it essentially is a Tag, leaving no need for a dedicated Tag class
    public static Note makeTag(Note note){
        return new Note(note.getTitle(), null, null, Utils.exactTime(), Utils.exactTime(), note.getTags());
    }

    // makes a completely new Tag for the current user
    public static Note newTag(String owner, String title){
        return makeTag(new Note(owner, new ArrayList<String>()));
    }

    // an interface that lets Util open the NoteActivity as needed
    interface ActivityInterp {
        void openNoteActivity(Note note);
        void openTagFragment(String currentTag);
    }
}
