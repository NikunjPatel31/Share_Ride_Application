package com.example.shareride.Screens;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.R;

public class MyAvailableRideAdapter extends RecyclerView.Adapter<MyAvailableRideAdapter.ViewHolder>{

    private static final String TAG = "MyAvailableRide";
    MyAvailableRideData[] myAvailableRideData;
    Context context;

    public MyAvailableRideAdapter(MyAvailableRideData[] myAvailableRideData,AvailableRide activity) {
        this.myAvailableRideData=myAvailableRideData;
        this.context=activity;
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

        final MyAvailableRideData myAvailableRide = myAvailableRideData[position];

        holder.ownername.setText(myAvailableRide.getOwnerName());
        holder.price.setText(myAvailableRide.getPrice());
        holder.sourcelocation.setText(myAvailableRide.getSourceLocation());
        holder.destinationLocation.setText(myAvailableRide.getDestinationLocation());
        holder.time.setText(myAvailableRide.getTime());
        //Log.d(TAG, "onBindViewHolder: Rating: "+myAvailableRide.getRating());
        holder.rating.setText(String.valueOf(myAvailableRide.getRating()));
        holder.date.setText(myAvailableRide.getDate());
        holder.seats.setText(myAvailableRide.getAvailableSeats());
        holder.profileimage.setImageResource(myAvailableRide.getProfileImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,myAvailableRide.getOwnerName(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myAvailableRideData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profileimage;
        TextView ownername;
        TextView price;
        TextView sourcelocation;
        TextView destinationLocation;
        TextView time;
        TextView date;
        TextView rating;
        RatingBar rbar;
        TextView seats;
        Button request;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.profile_image);
            ownername=itemView.findViewById(R.id.owner_name_text_view);
            price=itemView.findViewById(R.id.price_text_view);
            sourcelocation=itemView.findViewById(R.id.source_location_text_view);
            destinationLocation=itemView.findViewById(R.id.destination_location_text_view);
            time=itemView.findViewById(R.id.time_text_view);
            date=itemView.findViewById(R.id.date_text_view);
            rating=itemView.findViewById(R.id.rating_text_view);
            rbar=itemView.findViewById(R.id.ratingbar);
            seats=itemView.findViewById(R.id.seats_text_view);
            request=itemView.findViewById(R.id.request_btn);




        }
    }
}
