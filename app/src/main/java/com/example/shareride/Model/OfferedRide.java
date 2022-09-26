package com.example.shareride.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class OfferedRide implements Parcelable {
    LatLng sourceLocation, destinationLocation;
    String costPerSeats, date, time;
    int seats;
    String riderID;
    ArrayList<String> passengersIDList;
    ArrayList<String> preferencesList;
    String rideID;

    public OfferedRide() {}

    public OfferedRide(LatLng sourceLocation,
                       LatLng destinationLocation,
                       String costPerSeats,
                       String date,
                       String time,
                       int seats,
                       String riderID,
                       ArrayList<String> passengersIDList,
                       ArrayList<String> preferencesList,
                       String rideID) {
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.costPerSeats = costPerSeats;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.riderID = riderID;
        this.passengersIDList = passengersIDList;
        this.preferencesList = preferencesList;
        this.rideID = rideID;
    }

    protected OfferedRide(Parcel in) {
        costPerSeats = in.readString();
        date = in.readString();
        time = in.readString();
        seats = in.readInt();
        riderID = in.readString();
        passengersIDList = in.createStringArrayList();
        preferencesList = in.createStringArrayList();
        rideID = in.readString();
    }

    public static final Creator<OfferedRide> CREATOR = new Creator<OfferedRide>() {
        @Override
        public OfferedRide createFromParcel(Parcel in) {
            return new OfferedRide(in);
        }

        @Override
        public OfferedRide[] newArray(int size) {
            return new OfferedRide[size];
        }
    };

    public LatLng getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(LatLng sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getCostPerSeats() {
        return costPerSeats;
    }

    public void setCostPerSeats(String costPerSeats) {
        this.costPerSeats = costPerSeats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getRiderID() {
        return riderID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    public ArrayList<String> getPassengersIDList() {
        return passengersIDList;
    }

    public void setPassengersIDList(ArrayList<String> passengersIDList) {
        this.passengersIDList = passengersIDList;
    }

    public ArrayList<String> getPreferencesList() {
        return preferencesList;
    }

    public void setPreferencesList(ArrayList<String> preferencesList) {
        this.preferencesList = preferencesList;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(costPerSeats);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeInt(seats);
        parcel.writeString(riderID);
        parcel.writeStringList(passengersIDList);
        parcel.writeStringList(preferencesList);
        parcel.writeString(rideID);
    }
}
