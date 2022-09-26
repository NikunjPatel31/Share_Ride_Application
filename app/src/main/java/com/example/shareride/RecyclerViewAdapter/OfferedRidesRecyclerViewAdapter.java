package com.example.shareride.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.OfferedRide;
import com.example.shareride.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class OfferedRidesRecyclerViewAdapter extends RecyclerView.Adapter<OfferedRidesRecyclerViewAdapter.OfferedRidesViewHolder> {

    private ArrayList<OfferedRide> list;

    public OfferedRidesRecyclerViewAdapter(ArrayList<OfferedRide> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OfferedRidesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.offer_ride_item_layout,parent,false);
        return new OfferedRidesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferedRidesViewHolder holder, int position) {
        LatLng sourceLocation = list.get(position).getSourceLocation();
        LatLng destinationLocation = list.get(position).getDestinationLocation();
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvCostPerSeats.setText(list.get(position).getCostPerSeats()+" Rs");
        holder.tvSeats.setText(""+list.get(position).getSeats());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OfferedRidesViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSourceLocation,
                tvDestinationLocation,
                tvDate,
                tvTime,
                tvSeats,
                tvCostPerSeats;

        private AppCompatButton cancelBtn;
        public OfferedRidesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSourceLocation = itemView.findViewById(R.id.source_location_text_view);
            tvDestinationLocation = itemView.findViewById(R.id.destination_location_text_view);
            tvDate = itemView.findViewById(R.id.date_value_text_view);
            tvTime = itemView.findViewById(R.id.time_value_text_view);
            tvSeats = itemView.findViewById(R.id.seats_value_text_view);
            tvCostPerSeats = itemView.findViewById(R.id.cost_value_textView);
            cancelBtn = itemView.findViewById(R.id.cancel_button);
        }
    }
}
