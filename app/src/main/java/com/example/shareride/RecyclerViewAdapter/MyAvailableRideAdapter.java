package com.example.shareride.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.MyAvailableRideData;
import com.example.shareride.R;
import com.example.shareride.Screens.AvailableRide;
import com.example.shareride.Screens.RideDetail;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StampStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyAvailableRideAdapter extends RecyclerView.Adapter<MyAvailableRideAdapter.ViewHolder>{

    public interface RequestRideListener {
        void onRequestForRide(MyAvailableRideData rideData);
    }

    private static final String TAG = "MyAvailableRide";
    ArrayList<MyAvailableRideData> list;
    Context context;

    // firebase instances
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static RequestRideListener requestRideListener;

    public static void setOnRequestRideListener(RequestRideListener mListener) {
        requestRideListener = mListener;
    }

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

        db.collection("Ride Request")
                .whereEqualTo("RideID", list.get(position).getOfferedRide().getRideID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = document.get("Status").toString();
                                if (status.equals("Pending")) {
                                    holder.request.setText("Pending");
                                } else if (status.equals("Accepted")) {
                                    holder.request.setText("Accepted");
                                }
                            }
                        } else {
                            Toast.makeText(context, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        MyAvailableRideData ride = list.get(position);

        holder.time.setText(ride.getOfferedRide().getTime());
        holder.date.setText(ride.getOfferedRide().getDate());
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

        holder.parentLayoutCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), RideDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Ride", list.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.request.getText().equals("Request")) {
                    // make a request for ride
                    Map<String, Object> map = new HashMap<>();

                    map.put("PassengerID", mAuth.getCurrentUser().getUid());
                    map.put("RideID", list.get(holder.getAdapterPosition()).getOfferedRide().getRideID());
                    map.put("RiderID", list.get(holder.getAdapterPosition()).getOfferedRide().getRiderID());
                    map.put("Status", "Pending");
                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    map.put("Date", date);
                    db.collection("Ride Request")
                            .add(map)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // change the text of button to requested
                                    holder.request.setText("Requested");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error in making request", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // cancel already request ride
                }
            }
        });
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
        AppCompatButton request;
        CardView parentLayoutCV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price=itemView.findViewById(R.id.cost_value_textView);
            sourcelocation=itemView.findViewById(R.id.source_location_text_view);
            destinationLocation=itemView.findViewById(R.id.destination_location_text_view);
            time=itemView.findViewById(R.id.time_value_text_view);
            date=itemView.findViewById(R.id.date_value_text_view);
            request=itemView.findViewById(R.id.request_btn);
            parentLayoutCV = itemView.findViewById(R.id.available_rides_parent_layout_card_view);
        }
    }
}
