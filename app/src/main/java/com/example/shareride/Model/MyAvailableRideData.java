package com.example.shareride.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyAvailableRideData implements Parcelable {

    private OfferedRide offeredRide;
//    private User rider;

    public MyAvailableRideData(OfferedRide offeredRide) {
        this.offeredRide = offeredRide;
//        this.rider = rider;
    }

    protected MyAvailableRideData(Parcel in) {
        offeredRide = in.readParcelable(OfferedRide.class.getClassLoader());
//        rider = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<MyAvailableRideData> CREATOR = new Creator<MyAvailableRideData>() {
        @Override
        public MyAvailableRideData createFromParcel(Parcel in) {
            return new MyAvailableRideData(in);
        }

        @Override
        public MyAvailableRideData[] newArray(int size) {
            return new MyAvailableRideData[size];
        }
    };

    public OfferedRide getOfferedRide() {
        return offeredRide;
    }

    public void setOfferedRide(OfferedRide offeredRide) {
        this.offeredRide = offeredRide;
    }

//    public User getRider() {
//        return rider;
//    }

//    public void setRider(User rider) {
//        this.rider = rider;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(offeredRide, i);
//        parcel.writeParcelable(rider, i);
    }
}
