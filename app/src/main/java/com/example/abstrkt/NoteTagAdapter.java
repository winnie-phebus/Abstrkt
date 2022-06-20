package com.example.abstrkt;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class NoteTagAdapter extends RecyclerView.Adapter<NoteTagAdapter.PillHolder> {
    public static final String TAG = "NOTE_TAG_ADAPTER";
    List<String> items;

    public NoteTagAdapter(List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pill_button, parent, false);

        return new PillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PillHolder holder, int position) {
        String currentTag = getItems().get(position);
        holder.getButton().setText(currentTag);
        holder.getButton().setOnClickListener(tagPressed(currentTag));
    }

    private View.OnClickListener tagPressed(String currentTag) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: user clicked " + currentTag);
            }
        };
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    protected static class PillHolder extends RecyclerView.ViewHolder {
        MaterialButton button;

        public PillHolder(@NonNull View itemView) {
            super(itemView);
            //text = new TextView(itemView.getContext());
            button = itemView.findViewById(R.id.nt_button);
        }

        public MaterialButton getButton() {
            return button;
        }

        public void setButton(MaterialButton button) {
            this.button = button;
        }
    }
}
