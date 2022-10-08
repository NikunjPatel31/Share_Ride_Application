package com.example.shareride.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.R;

import java.util.ArrayList;

public class ShowPreferencesRecyclerAdapter extends RecyclerView.Adapter<ShowPreferencesRecyclerAdapter.ShowPreferencesViewHolder> {

    private ArrayList<String> preferencesList;

    public ShowPreferencesRecyclerAdapter(ArrayList<String> preferencesList) {
        this.preferencesList = preferencesList;
    }

    @NonNull
    @Override
    public ShowPreferencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.show_preference_item_layout, parent, false);
        return new ShowPreferencesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPreferencesViewHolder holder, int position) {
        holder.tvTitle.setText(preferencesList.get(position));
    }

    @Override
    public int getItemCount() {
        return preferencesList.size();
    }

    public static class ShowPreferencesViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ShowPreferencesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.preferences_title_text_view);
        }
    }
}
