package com.example.shareride.RecyclerViewAdapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.OfferedRide;
import com.example.shareride.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OfferedRidesRecyclerViewAdapter extends RecyclerView.Adapter<OfferedRidesRecyclerViewAdapter.OfferedRidesViewHolder> {

    private ArrayList<OfferedRide> list;
    private Context context;
    private static final String TAG = "OfferedRidesRecycler";
    private static RideDeleteListener rideDeleteListener;
    private static RideSelectListener rideSelectListener;

    public interface RideSelectListener {
        void onRideSelectListener(OfferedRide ride);
    }

    public interface RideDeleteListener {
        void onRideDeleteListener(int position, OfferedRide ride);
    }

    public static void setRideSelectListener(RideSelectListener mListener) {
        rideSelectListener = mListener;
    }

    public static void setRideDeleteListener(RideDeleteListener mListener) {
        rideDeleteListener = mListener;
    }

    public OfferedRidesRecyclerViewAdapter(ArrayList<OfferedRide> list, Context context) {
        this.list = list;
        this.context = context;
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

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(sourceLocation.latitude, sourceLocation.longitude,1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            holder.tvSourceLocation.setText(address+", "+cityName+", "+stateName);

            addresses = geocoder.getFromLocation(destinationLocation.latitude, destinationLocation.longitude, 1);

            address = addresses.get(0).getSubLocality();
            cityName = addresses.get(0).getLocality();
            stateName = addresses.get(0).getAdminArea();
            Log.d(TAG, "onBindViewHolder: Address: "+address+", "+cityName+", "+stateName);

            holder.tvDestinationLocation.setText(address+", "+cityName+", "+stateName);
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: Exception: "+e.getLocalizedMessage());
        }
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvCostPerSeats.setText(list.get(position).getCostPerSeats()+" Rs");
        holder.tvSeats.setText(""+list.get(position).getSeats());

        if (list.get(position).getStatus().equals("Completed")) {
            holder.cancelBtn.setVisibility(View.INVISIBLE);
        }

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideDeleteListener.onRideDeleteListener(holder.getAdapterPosition(),
                        list.get(holder.getAdapterPosition()));
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideSelectListener.onRideSelectListener(list.get(holder.getAdapterPosition()));
            }
        });
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

        private CardView parentLayout;

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
            parentLayout = itemView.findViewById(R.id.offer_ride_parent_layout);
        }
    }
}
