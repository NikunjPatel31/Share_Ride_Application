package com.example.shareride.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {

    String carName,
            fuelType,
            modelYear,
            vehicleNumber,
            airConditioner,
            carImage,
            userId,
            carId;

    public Car(String carName,
               String fuelType,
               String modelYear,
               String vehicleNumber,
               String airConditioner,
               String carImage,
               String userId,
               String carId) {

        this.carName = carName;
        this.fuelType = fuelType;
        this.modelYear = modelYear;
        this.vehicleNumber = vehicleNumber;
        this.airConditioner = airConditioner;
        this.carImage = carImage;
        this.userId = userId;
        this.carId = carId;
    }

    protected Car(Parcel in) {
        carName = in.readString();
        fuelType = in.readString();
        modelYear = in.readString();
        vehicleNumber = in.readString();
        airConditioner = in.readString();
        carImage = in.readString();
        userId = in.readString();
        carId = in.readString();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(String airConditioner) {
        this.airConditioner = airConditioner;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(carName);
        parcel.writeString(fuelType);
        parcel.writeString(modelYear);
        parcel.writeString(vehicleNumber);
        parcel.writeString(airConditioner);
        parcel.writeString(carImage);
        parcel.writeString(userId);
        parcel.writeString(carId);
    }
}
