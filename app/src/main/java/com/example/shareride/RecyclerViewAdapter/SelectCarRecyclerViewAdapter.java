package com.example.shareride.RecyclerViewAdapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.Car;
import com.example.shareride.R;
import com.example.shareride.Screens.CarDetails;
import com.example.shareride.Screens.MyAvailableRideAdapter;
import com.example.shareride.Screens.ViewMyCars;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectCarRecyclerViewAdapter extends RecyclerView.Adapter<SelectCarRecyclerViewAdapter.SelectCarViewHolder> {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Context context;
    private ArrayList<Car> carsList = new ArrayList<>();
    private static SelectCarListener selectCarListener;

    public interface SelectCarListener {
        void onCarSelectedListener(Car car);
    }

    public static void setSelectCarListener(SelectCarListener mListener) {
        selectCarListener = mListener;
    }

    public SelectCarRecyclerViewAdapter(ArrayList<Car> carsList, Context context) {
        this.context = context;
        this.carsList = carsList;
    }

    @NonNull
    @Override
    public SelectCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.select_car_item_layout,parent, false);
        return new SelectCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectCarViewHolder holder, int position) {
        holder.tvCarName.setText(carsList.get(position).getCarName());
        holder.tvVehicleNumber.setText(carsList.get(position).getVehicleNumber());
        holder.tvFuelType.setText(carsList.get(position).getFuelType());
        holder.tvAirConditioner.setText(carsList.get(position).getAirConditioner());
        holder.tvModelYear.setText(carsList.get(position).getModelYear());
        holder.setImage(carsList.get(position).getCarImage());

        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), CarDetails.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("Car", ViewMyCars.carList.get(holder.getAdapterPosition()));
//                context.startActivity(intent);
                selectCarListener.onCarSelectedListener(carsList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }

    public static class SelectCarViewHolder extends RecyclerView.ViewHolder {

        public CardView parentCardView;
        public TextView tvCarName, tvVehicleNumber, tvFuelType, tvAirConditioner, tvModelYear;
        public ImageView imgCarImage;

        public SelectCarViewHolder(@NonNull View itemView) {
            super(itemView);
            parentCardView = itemView.findViewById(R.id.inner_cardview);
            tvCarName = itemView.findViewById(R.id.car_name_textview);
            tvVehicleNumber = itemView.findViewById(R.id.vehicle_number_text_view);
            tvFuelType = itemView.findViewById(R.id.fuel_text_view);
            tvAirConditioner = itemView.findViewById(R.id.air_conditioner_text_view);
            tvModelYear = itemView.findViewById(R.id.model_year_text_view);
            imgCarImage = itemView.findViewById(R.id.car_image_view);
        }

        public void setImage(String imageLink) {
            Picasso.get().load(Uri.parse(imageLink)).into(imgCarImage);
        }
    }
}
