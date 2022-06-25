package com.example.abstrkt;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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


}