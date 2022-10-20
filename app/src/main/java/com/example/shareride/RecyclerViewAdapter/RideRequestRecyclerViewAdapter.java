package com.example.shareride.RecyclerViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.OfferedRide;
import com.example.shareride.Model.RideRequest;
import com.example.shareride.Model.User;
import com.example.shareride.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RideRequestRecyclerViewAdapter extends RecyclerView.Adapter<RideRequestRecyclerViewAdapter.RideRequestViewHolder> {

    // firebase instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // local instances variable
    ArrayList<RideRequest> list;
    Context context;
    static final String TAG = "RideRequest";
    static ActionListener actionListener;

    public interface ActionListener {
        void onActionListener(int position);
    }

    public static void setActionListener(ActionListener mListener) {
        actionListener = mListener;
    }

    public RideRequestRecyclerViewAdapter(ArrayList<RideRequest> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RideRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ride_request_item_layout, parent, false);
        return new RideRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideRequestViewHolder holder, int position) {
        RideRequest ride = list.get(position);

        final OfferedRide[] offeredRide = {null};

        db.collection("Offer Ride")
                .document(ride.getRideID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            //offeredRide.setSourceLocation(documentSnapshot.get("Source Location"));

                            HashMap<String, Double> map = (HashMap<String, Double>) documentSnapshot.get("Source Location");
                            LatLng sourceLocation = new LatLng(map.get("latitude"), map.get("longitude"));

                            map = (HashMap<String, Double>) documentSnapshot.get("Destination Location");
                            LatLng destinationLocation = new LatLng(map.get("latitude"), map.get("longitude"));

                            offeredRide[0] = new OfferedRide();
                            offeredRide[0].setRideID(documentSnapshot.getId());
                            offeredRide[0].setSourceLocation(sourceLocation);
                            offeredRide[0].setDestinationLocation(destinationLocation);
                            offeredRide[0].setDate(documentSnapshot.get("Date").toString());
                            offeredRide[0].setCostPerSeats(documentSnapshot.get("Cost Per Seats").toString());
                            offeredRide[0].setSeats(Integer.parseInt(documentSnapshot.get("Seats").toString()));
                            offeredRide[0].setTime(documentSnapshot.get("Time").toString());
                            offeredRide[0].setRiderID(documentSnapshot.get("RiderID").toString());
                            offeredRide[0].setPassengersIDList((ArrayList<String>) documentSnapshot.get("PassengerList"));
                            Log.d(TAG, "onComplete: Passenger List: "+offeredRide[0].getPassengersIDList());//offeredRide[0].getPassengersIDList());
                            holder.tvSourceLocationName.setText(documentSnapshot.get("Source Location Name").toString());
                            holder.tvDestinationLocationName.setText(documentSnapshot.get("Destination Location Name").toString());
                            holder.tvCost.setText("₹ "+documentSnapshot.get("Cost Per Seats").toString());
                            holder.tvDate.setText(documentSnapshot.get("Date").toString());
                            holder.tvSeats.setText(documentSnapshot.get("Seats").toString());

                        } else {
                            Log.d(TAG, "onComplete: Exception: "+task.getException());
                        }
                    }
                });

        databaseReference.child("Users")
                        .child(ride.getPassengerID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = new User();

                                user.setFirstName(snapshot.child("First_Name").getValue().toString());
                                user.setLastName(snapshot.child("Last_Name").getValue().toString());
                                user.setGender(snapshot.child("Gender").getValue().toString());
                                user.setPincode(snapshot.child("Pincode").getValue().toString());
                                user.setProfilePic(snapshot.child("Profile_picture").getValue().toString());
                                user.setCity(snapshot.child("City").getValue().toString());
                                user.setContact(snapshot.child("Contact").getValue().toString());
                                user.setDOB(snapshot.child("DOB").getValue().toString());
                                user.setUserID(snapshot.getKey());

                                holder.tvPassengerName.setText(user.getFirstName() +" " + user.getLastName());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Ride Request")
                        .document(ride.getRideRequestID())
                        .update("Status", "Accepted")
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error in accepting the request", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                            }
                        });


                ArrayList<String> ids = offeredRide[0].getPassengersIDList();
                ids.add(ride.getPassengerID());

                db.collection("Offer Ride")
                        .document(ride.getRideID())
                        .update("PassengerList",ids,"Seats", offeredRide[0].getSeats()-1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                actionListener.onActionListener(holder.getAdapterPosition());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                            }
                        });
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Ride Request")
                        .document(ride.getRideRequestID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                actionListener.onActionListener(holder.getAdapterPosition());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error in rejecting", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: Exception: "+e.getLocalizedMessage());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RideRequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvPassengerName,
                tvSourceLocationName,
                tvDestinationLocationName,
                tvDate,
                tvSeats,
                tvCost;

        AppCompatButton btnReject, btnAccept;
        public RideRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPassengerName = itemView.findViewById(R.id.passenger_name_text_view);
            tvSourceLocationName = itemView.findViewById(R.id.source_location_text_view);
            tvDestinationLocationName = itemView.findViewById(R.id.destination_location_text_view);
            tvDate = itemView.findViewById(R.id.date_value_text_view);
            tvSeats = itemView.findViewById(R.id.seats_value_text_view);
            tvCost = itemView.findViewById(R.id.cost_value_textView);
            btnAccept = itemView.findViewById(R.id.accept_button);
            btnReject = itemView.findViewById(R.id.reject_button);
        }
    }
}
