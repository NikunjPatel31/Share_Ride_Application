package com.example.shareride.RecyclerViewAdapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.Car;
import com.example.shareride.R;
import com.example.shareride.Screens.CarDetails;
import com.example.shareride.Screens.ViewMyCars;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarsRecyclerViewAdapter extends RecyclerView.Adapter<CarsRecyclerViewAdapter.CarViewHolder> {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Context context;
    public CarsRecyclerViewAdapter(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_my_cars_layout,parent, false);
        CarViewHolder carViewHolder = new CarViewHolder(view);
        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.tvCarName.setText(ViewMyCars.carList.get(position).getCarName());
        holder.tvVehicleNumber.setText(ViewMyCars.carList.get(position).getVehicleNumber());
        Log.d(TAG, "onBindViewHolder: vehicleNumber: "+ViewMyCars.carList.get(position).getVehicleNumber());
        holder.tvFuelType.setText(ViewMyCars.carList.get(position).getFuelType());
        holder.tvAirConditioner.setText(ViewMyCars.carList.get(position).getAirConditioner());
        holder.tvModelYear.setText(ViewMyCars.carList.get(position).getModelYear());
        holder.setImage(ViewMyCars.carList.get(position).getCarImage());

        holder.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Cars")
                        .child(ViewMyCars.carList.get(holder.getAdapterPosition()).getCarId())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ViewMyCars.carList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                storageReference.child("Car_Images")
                        .child(ViewMyCars.carList.get(holder.getAdapterPosition()).getUserId())
                        .child(ViewMyCars.carList.get(holder.getAdapterPosition()).getCarId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: Car delete Successful" );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Car Image delete fail Exception: "+e.getLocalizedMessage());
                            }
                        });
            }
        });

        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), CarDetails.class);
                
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ViewMyCars.carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public CardView parentCardView;
        public TextView tvCarName, tvVehicleNumber, tvFuelType, tvAirConditioner, tvModelYear;
        public FloatingActionButton fabDelete;
        public ImageView imgCarImage;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            parentCardView = itemView.findViewById(R.id.inner_cardview);
            tvCarName = itemView.findViewById(R.id.car_name_textview);
            tvVehicleNumber = itemView.findViewById(R.id.vehicle_number_text_view);
            tvFuelType = itemView.findViewById(R.id.fuel_text_view);
            tvAirConditioner = itemView.findViewById(R.id.air_conditioner_text_view);
            tvModelYear = itemView.findViewById(R.id.model_year_text_view);
            fabDelete = itemView.findViewById(R.id.delete_car_fab);
            imgCarImage = itemView.findViewById(R.id.car_image_view);

        }

        public void setImage(String imageLink) {
            Picasso.get().load(Uri.parse(imageLink)).into(imgCarImage);
            //Picasso.get().load(Uri.parse(imageLink)).into(cirImgCar);
        }
    }
}
