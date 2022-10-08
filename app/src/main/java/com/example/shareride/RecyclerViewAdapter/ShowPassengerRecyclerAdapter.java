package com.example.shareride.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareride.Model.User;
import com.example.shareride.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowPassengerRecyclerAdapter extends RecyclerView.Adapter<ShowPassengerRecyclerAdapter.ShowPassengerViewHolder> {
    private ArrayList<User> userList;

    public ShowPassengerRecyclerAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ShowPassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.show_passenger_item_layout, parent, false);
        return new ShowPassengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPassengerViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUserName.setText(user.getFirstName() + " " + user.getLastName());
        holder.tvGender.setText(user.getGender());
        Picasso.get().load(user.getProfilePic()).into(holder.cirImgUser);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ShowPassengerViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvGender;
        CircleImageView cirImgUser;

        public ShowPassengerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.passenger_name_text_view);
            tvGender = itemView.findViewById(R.id.gender_text_view);
            cirImgUser = itemView.findViewById(R.id.passenger_profile_image_view);
        }
    }
}
