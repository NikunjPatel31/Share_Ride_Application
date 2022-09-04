package com.example.shareride.Model;

public class Car {

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
}
