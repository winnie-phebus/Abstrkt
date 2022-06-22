package com.example.abstrkt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderHolder extends RecyclerView.ViewHolder {

    private ImageView folderIcon;
    private TextView folderName;
    private ConstraintLayout layout;

    public FolderHolder(@NonNull View itemView) {
        super(itemView);

        this.folderIcon = itemView.findViewById(R.id.folder_prev_icon);
        this.folderName = itemView.findViewById(R.id.folder_prev_name);
        this.layout = itemView.findViewById(R.id.folder_outsideContainer);
    }

    public ImageView getFolderIcon() {
        return folderIcon;
    }

    public void setFolderIcon(ImageView folderIcon) {
        this.folderIcon = folderIcon;
    }

    public TextView getFolderName() {
        return folderName;
    }

    public void setFolderName(TextView folderName) {
        this.folderName = folderName;
    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public void setLayout(ConstraintLayout layout) {
        this.layout = layout;
    }

    // might be referencing later
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

            }
        });

        return rootView;
    }
}