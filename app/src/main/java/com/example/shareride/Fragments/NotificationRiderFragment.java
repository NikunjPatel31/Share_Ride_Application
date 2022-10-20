package com.example.shareride.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shareride.Model.RideRequest;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.RideRequestRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationRiderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationRiderFragment extends Fragment {

    // component instances
    RecyclerView rideRequestRecyclerView;

    // firebase instances
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // local instance variables
    ArrayList<RideRequest> rideRequestList = new ArrayList<>();
    RideRequestRecyclerViewAdapter adapter = new RideRequestRecyclerViewAdapter(new ArrayList<RideRequest>(), getContext());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationRiderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationRiderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationRiderFragment newInstance(String param1, String param2) {
        NotificationRiderFragment fragment = new NotificationRiderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_rider, container, false);
        initializeComponents(view);
        rideRequestRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        rideRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RideRequestRecyclerViewAdapter.setActionListener(new RideRequestRecyclerViewAdapter.ActionListener() {
            @Override
            public void onActionListener(int position) {
                rideRequestList.remove(position+1);
                adapter.notifyItemRemoved(position);
            }
        });
        return view;
    }

    private void initializeComponents(View view) {
        // this method will initialize all the components
        rideRequestRecyclerView = view.findViewById(R.id.ride_request_recycler_view);
    }

    private void fetchRequest() {
        // this method will fetch all request from firebase firestore
        db.collection("Ride Request")
                .whereEqualTo("RiderID", mAuth.getCurrentUser().getUid())
                .whereEqualTo("Status", "Pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (QueryDocumentSnapshot document : value) {
                            RideRequest ride = new RideRequest();
                            ride.setRideRequestID(document.getId());
                            ride.setPassengerID(document.get("PassengerID").toString());
                            ride.setStatus(document.get("Status").toString());
                            ride.setRideID(document.get("RideID").toString());

                            rideRequestList.add(ride);
                        }
                        // setting recyclerview adapter
                        adapter = new RideRequestRecyclerViewAdapter(rideRequestList, getContext());
                        rideRequestRecyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rideRequestList.clear();
    }
}