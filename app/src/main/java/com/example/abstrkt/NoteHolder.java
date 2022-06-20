package com.example.abstrkt;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

// TODO: may be useful: https://stackoverflow.com/questions/42450466/two-arraylist-one-recyclerview-adapter
public class NoteHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout container;
    private TextView title;
    private TextView noteSummary;
    private RecyclerView tags;
    public NoteHolder(@NonNull View itemView) {
        super(itemView);
        this.container = itemView.findViewById(R.id.np_outercontainer);
        this.title = itemView.findViewById(R.id.np_title);
        this.noteSummary = itemView.findViewById(R.id.np_summary);

        this.tags = itemView.findViewById(R.id.np_tagsRV);
    }

    public ConstraintLayout getContainer() {
        return container;
    }

    public void setContainer(ConstraintLayout container) {
        this.container = container;
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
