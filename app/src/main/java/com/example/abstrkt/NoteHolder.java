package com.example.abstrkt;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView noteSummary;
    private RecyclerView tags;

    public NoteHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.np_title);
        this.noteSummary = itemView.findViewById(R.id.np_summary);
        this.tags = itemView.findViewById(R.id.np_tagsRV);
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getNoteSummary() {
        return noteSummary;
    }

    public void setNoteSummary(TextView noteSummary) {
        this.noteSummary = noteSummary;
    }

    public RecyclerView getTags() {
        return tags;
    }

    public void setTags(RecyclerView tags) {
        this.tags = tags;
    }
}
