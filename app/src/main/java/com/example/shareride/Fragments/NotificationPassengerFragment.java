package com.example.shareride.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareride.Model.RideRequest;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.PassengerReqAckRecyclerViewAdapter;
import com.example.shareride.RecyclerViewAdapter.RideRequestRecyclerViewAdapter;
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
 * Use the {@link NotificationPassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationPassengerFragment extends Fragment {

    // Components
    RecyclerView recyclerView;

    // firebase instance variable
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // local instance variable
    ArrayList<RideRequest> rideRequestList = new ArrayList<>();
    PassengerReqAckRecyclerViewAdapter adapter = new PassengerReqAckRecyclerViewAdapter(new ArrayList<>(), getContext());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationPassengerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationPassengerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationPassengerFragment newInstance(String param1, String param2) {
        NotificationPassengerFragment fragment = new NotificationPassengerFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification_passenger, container, false);
        initializeComponents(view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PassengerReqAckRecyclerViewAdapter.setActionListener(new RideRequestRecyclerViewAdapter.ActionListener() {
            @Override
            public void onActionListener(int position) {
                try {
                    rideRequestList.remove(position+1);
                    adapter.notifyItemRemoved(position);
                } catch (Exception e) {
                    Log.d(TAG, "onActionListener: Exception: "+e.getLocalizedMessage());
                }
            }
        });
        return view;
    }

    private void initializeComponents(View view) {
        // this method will initialize all the components
        recyclerView = view.findViewById(R.id.passenger_recycler_view);
    }

    private void fetchDetails() {
        // this method will fetch details from "Ride Request" collection
        db.collection("Ride Request")
                .whereEqualTo("PassengerID",mAuth.getCurrentUser().getUid())
                .whereEqualTo("Status", "Accepted")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (QueryDocumentSnapshot document : value) {
                            RideRequest ride = new RideRequest();
                            ride.setRideRequestID(document.getId());
                            ride.setPassengerID(document.get("PassengerID").toString());
                            ride.setStatus(document.get("Status").toString());
                            ride.setRideID(document.get("RideID").toString());
                            ride.setRiderID(document.get("RiderID").toString());

                            rideRequestList.add(ride);
                        }
                        adapter = new PassengerReqAckRecyclerViewAdapter(rideRequestList, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchDetails();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rideRequestList.clear();
    }
}