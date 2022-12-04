package com.example.shareride.Fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_APPEND;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shareride.Model.RideRequest;
import com.example.shareride.R;
import com.example.shareride.RecyclerViewAdapter.PassengerReqAckRecyclerViewAdapter;
import com.example.shareride.RecyclerViewAdapter.RideRequestRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

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
    final int UPI_PAYMENT = 0;
    private static final String TAG = NotificationPassengerFragment.class.getSimpleName();

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

        PassengerReqAckRecyclerViewAdapter.setPaymentButtonListener(new PassengerReqAckRecyclerViewAdapter.PaymentButtonListener() {
            @Override
            public void onPaymentClickListener(RideRequest ride, String amount) {

                SharedPreferences sh = getContext().getSharedPreferences("UserSharedPreferences", Context.MODE_PRIVATE);

                String riderName = sh.getString("First Name", "") + " " + sh.getString("Last Name", "");//"Nikunj";
                //"vaghasiyakeyur981@oksbi";
                String contact = sh.getString("Contact", "9484553118");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Users").child(ride.getRiderID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("First_Name").getValue().toString() + " " + snapshot.child("Last_Name").getValue().toString();
                                String upiId = snapshot.child("UPI_ID").getValue().toString();

                                Log.d(TAG, "onPaymentClickListener: Name: "+name);
                                Log.d(TAG, "onPaymentClickListener: upiId: "+upiId);
                                Log.d(TAG, "onPaymentClickListener: amount: "+amount);

//                                startPayment(name, Integer.parseInt(amount), contact);

                                Uri uri = Uri.parse("upi://pay").buildUpon()
                                        .appendQueryParameter("pa", upiId)
                                        .appendQueryParameter("pn", name)
                                        //.appendQueryParameter("mc", "")
                                        //.appendQueryParameter("tid", "02125412")
                                        //.appendQueryParameter("tr", "25584584")
                                        .appendQueryParameter("am", amount)
                                        .appendQueryParameter("cu", "INR")
                                        //.appendQueryParameter("refUrl", "blueapp")
                                        .build();

                                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                                upiPayIntent.setData(uri);

                                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
                                // check if intent resolves
                                if(null != chooser.resolveActivity(getContext().getPackageManager())) {
                                    startActivityForResult(chooser, UPI_PAYMENT);
                                } else {
                                    //Toast.makeText(getContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onDataChange: No UPI app found, please install one to continue");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }

    public void startPayment(String riderName, int amount, String contact) {
        Checkout.preload(getContext());
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_R9K3FPDkkrzIFG");
        /**
         * Instantiate Checkout
         */

        /**
         * Set your logo here
         */

        /**
         * Reference to current activity
         */

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", riderName);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amount * 100);//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", contact);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

            checkout.open((Activity) getContext(), options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
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
        Toast.makeText(getContext(), "onResume", Toast.LENGTH_SHORT).show();
        rideRequestList.clear();
        fetchDetails();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getContext(), "onStop", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStop: we are inside the onStop");
        rideRequestList.clear();
    }
}