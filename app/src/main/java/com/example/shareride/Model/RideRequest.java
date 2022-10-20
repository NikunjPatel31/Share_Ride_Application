package com.example.shareride.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RideRequest implements Parcelable {
    String riderID;
    String rideRequestID;
    String passengerID;
    String status;
    String rideID;

    public RideRequest() {}

    public RideRequest(String rideRequestID, String passengerID, String status, String rideID, String riderID) {
        this.rideRequestID = rideRequestID;
        this.passengerID = passengerID;
        this.status = status;
        this.rideID = rideID;
        this.riderID = riderID;
    }

    protected RideRequest(Parcel in) {
        rideRequestID = in.readString();
        passengerID = in.readString();
        status = in.readString();
        rideID = in.readString();
        riderID = in.readString();
    }

    public static final Creator<RideRequest> CREATOR = new Creator<RideRequest>() {
        @Override
        public RideRequest createFromParcel(Parcel in) {
            return new RideRequest(in);
        }

        @Override
        public RideRequest[] newArray(int size) {
            return new RideRequest[size];
        }
    };

    public String getRideRequestID() {
        return rideRequestID;
    }

    public void setRideRequestID(String rideRequestID) {
        this.rideRequestID = rideRequestID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getRiderID() {
        return riderID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rideRequestID);
        parcel.writeString(passengerID);
        parcel.writeString(status);
        parcel.writeString(rideID);
        parcel.writeString(riderID);
    }
}
