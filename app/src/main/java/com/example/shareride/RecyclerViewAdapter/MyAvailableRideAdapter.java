package com.example.shareride.RecyclerViewAdapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.MyAvailableRideData;
import com.example.shareride.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAvailableRideAdapter extends RecyclerView.Adapter<MyAvailableRideAdapter.ViewHolder>{

    private static final String TAG = "MyAvailableRide";
    ArrayList<MyAvailableRideData> list;
    Context context;

    public MyAvailableRideAdapter(ArrayList<MyAvailableRideData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from((parent.getContext()));
        View view=layoutInflater.inflate(R.layout.available_ride_list,parent,false);
        ViewHolder viewHolder = new ViewHolder((view));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyAvailableRideData ride = list.get(position);

        holder.time.setText(ride.getOfferedRide().getTime());
        holder.date.setText(ride.getOfferedRide().getDate());
        holder.seats.setText(""+ride.getOfferedRide().getSeats());
        holder.price.setText(ride.getOfferedRide().getCostPerSeats() + " ₹");

        LatLng sourceLatLng = ride.getOfferedRide().getSourceLocation();
        LatLng destinationLatLng = ride.getOfferedRide().getDestinationLocation();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(sourceLatLng.latitude, sourceLatLng.longitude,1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            holder.sourcelocation.setText(address+", "+cityName+", "+stateName);

            addresses = geocoder.getFromLocation(destinationLatLng.latitude, destinationLatLng.longitude, 1);

            address = addresses.get(0).getSubLocality();
            cityName = addresses.get(0).getLocality();
            stateName = addresses.get(0).getAdminArea();
            Log.d(TAG, "onBindViewHolder: Address: "+address+", "+cityName+", "+stateName);

            holder.destinationLocation.setText(address+", "+cityName+", "+stateName);
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: Exception: "+e.getLocalizedMessage());
        }

//        holder.price.setText(ride.getOfferedRide().getCostPerSeats());

        /*
        convert the source location latitude and longitude to location name
        holder.sourcelocation.setText(ride.getOfferedRide());
        */

        /*
        convert the destination location latitude and longitude to location name
        holder.destinationLocation.setText(myAvailableRide.getDestinationLocation());
        */

//        holder.time.setText(ride.getOfferedRide().getTime());
//        //Log.d(TAG, "onBindViewHolder: Rating: "+myAvailableRide.getRating());
//        holder.date.setText(ride.getOfferedRide().getDate());
//        holder.seats.setText(ride.getOfferedRide().getSeats());

        /*
        set user profile picture
        holder.profileimage.setImageResource(myAvailableRide.getProfileImage());
        */

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context,myAvailableRide.getOwnerName(),Toast.LENGTH_LONG).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView price;
        TextView sourcelocation;
        TextView destinationLocation;
        TextView time;
        TextView date;
        TextView seats;
        AppCompatButton request;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price=itemView.findViewById(R.id.cost_value_textView);
            sourcelocation=itemView.findViewById(R.id.source_location_text_view);
            destinationLocation=itemView.findViewById(R.id.destination_location_text_view);
            time=itemView.findViewById(R.id.time_value_text_view);
            date=itemView.findViewById(R.id.date_value_text_view);
            seats=itemView.findViewById(R.id.seats_value_text_view);
            request=itemView.findViewById(R.id.request_btn);
        }
    }
}
