package com.example.shareride.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PreferencesOptionRecyclerViewAdapter extends RecyclerView.Adapter<PreferencesOptionRecyclerViewAdapter.PreferencesOptionViewHolder> {

    ArrayList<String> preferenceList;
    Context context;
    PreferenceOptionInterface preferenceOptionListener;

    public interface PreferenceOptionInterface {
        void onPreferenceSelected(int position);
    }

    public void setPreferenceListener(PreferenceOptionInterface mListener) {
        preferenceOptionListener = mListener;
    }

    public PreferencesOptionRecyclerViewAdapter(ArrayList<String> preferenceList, Context context) {
        this.preferenceList = preferenceList;
        this.context = context;
    }

    @NonNull
    @Override
    public PreferencesOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.preferences_options_list, parent, false);
        return new PreferencesOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferencesOptionViewHolder holder, int position) {
        holder.tvPreferenceName.setText(preferenceList.get(holder.getAdapterPosition()));
        holder.addFABBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceOptionListener.onPreferenceSelected(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return preferenceList.size();
    }

    public static class PreferencesOptionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPreferenceName;
        private FloatingActionButton addFABBtn;

        public PreferencesOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPreferenceName = itemView.findViewById(R.id.preference_name_text_View);
            addFABBtn = itemView.findViewById(R.id.add_fab_btn);
        }
    }
}
