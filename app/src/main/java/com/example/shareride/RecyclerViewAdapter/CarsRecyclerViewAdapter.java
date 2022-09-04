package com.example.shareride.RecyclerViewAdapter;

import static android.content.ContentValues.TAG;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarsRecyclerViewAdapter extends RecyclerView.Adapter<CarsRecyclerViewAdapter.CarViewHolder> {
    private ArrayList<Car> carList;

    public CarsRecyclerViewAdapter(ArrayList<Car> carList) {
        this.carList = carList;
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
        holder.tvCarName.setText(carList.get(position).getCarName());
        holder.tvVehicleNumber.setText(carList.get(position).getVehicleNumber());
        Log.d(TAG, "onBindViewHolder: vehicleNumber: "+carList.get(position).getVehicleNumber());
        holder.tvFuelType.setText(carList.get(position).getFuelType());
        holder.tvAirConditioner.setText(carList.get(position).getAirConditioner());
        holder.tvModelYear.setText(carList.get(position).getModelYear());
        holder.setImage(carList.get(position).getCarImage());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public CardView parentCardView;
        public TextView tvCarName, tvVehicleNumber, tvFuelType, tvAirConditioner, tvModelYear;
        public Button btnEdit, btnDelete;
        public ImageView imgCarImage;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            parentCardView = itemView.findViewById(R.id.inner_cardview);
            tvCarName = itemView.findViewById(R.id.car_name_textview);
            tvVehicleNumber = itemView.findViewById(R.id.vehicle_number_text_view);
            tvFuelType = itemView.findViewById(R.id.fuel_text_view);
            tvAirConditioner = itemView.findViewById(R.id.air_conditioner_text_view);
            tvModelYear = itemView.findViewById(R.id.model_year_text_view);

            //btnDelete = itemView.findViewById(R.id.delete_button);
            imgCarImage = itemView.findViewById(R.id.car_image_view);

        }

        public void setImage(String imageLink) {
            Picasso.get().load(Uri.parse(imageLink)).into(imgCarImage);
            //Picasso.get().load(Uri.parse(imageLink)).into(cirImgCar);
        }
    }
}
