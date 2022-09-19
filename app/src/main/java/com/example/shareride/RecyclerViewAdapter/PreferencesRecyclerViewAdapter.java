package com.example.shareride.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.R;

import java.util.ArrayList;

public class PreferencesRecyclerViewAdapter extends RecyclerView.Adapter<PreferencesRecyclerViewAdapter.PreferencesRecyclerViewHolder> {

    ArrayList<String> list;
    Context context;

    public PreferencesRecyclerViewAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PreferencesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.preference_item_layout, parent, false);
        return new PreferencesRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferencesRecyclerViewHolder holder, int position) {
        holder.tvPreferenceTitle.setText(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PreferencesRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPreferenceTitle;
        public PreferencesRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPreferenceTitle = itemView.findViewById(R.id.preferences_title_text_view);
        }
    }
}
