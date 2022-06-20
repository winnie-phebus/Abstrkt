package com.example.abstrkt;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderHolder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderHolder extends Fragment {

    ImageView folderIcon;
    TextView folderName;
    ConstraintLayout layout;

    public FolderHolder() {
        // Required empty public constructor
    }


    public static FolderHolder newInstance(String param1, String param2) {
        FolderHolder fragment = new FolderHolder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.preview_folder, container, false);

        folderIcon = rootView.findViewById(R.id.folder_prev_icon);
        folderName = rootView.findViewById(R.id.folder_prev_name);

        layout = rootView.findViewById(R.id.folder_outsideContainer);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO on click open a recylcer view below with all notes in that folder
                // change state of foler to open -> change icon to folder_open ressource file
                // click to close
            }
        });



        return rootView;
    }
}