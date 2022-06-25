package com.example.abstrkt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashFragment extends Fragment {
    private FirebaseUser user;

    public TrashFragment() {
        // Required empty public constructor
    }

    public static TrashFragment newInstance() {
        return new TrashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trash, container, false);

        Button clearTrash = v.findViewById(R.id.trash_clear);
        clearTrash.setOnClickListener(deleteAllTrash());
        RecyclerView trashNotes = v.findViewById(R.id.arc_rv);

        trashNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        trashNotes.setAdapter(
                Utils.buildNoteAdapter(getContext(),
                        Utils.buildStatusQuery(user.getDisplayName(), Utils.N_TRASH)));
        return v;
    }

    private View.OnClickListener deleteAllTrash() {
        return view -> {
            Utils.toast(getContext(), "Working on it! :P");
            // TODO: implement this! :) -- figure out how to traverse the query list and delete
        };
    }
}