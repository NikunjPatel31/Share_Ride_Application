package com.example.shareride.Screens;

public class MyAvailableRideData {



    private String ownerName;
    private String price;
    private String sourceLocation;
    private String destinationLocation;
    private String time;
    private int Rating;
    private String date;
    private String availableSeats;
    private int profileImage;

    public MyAvailableRideData(String ownerName, String price, String sourceLocation, String destinationLocation, String time, int rating, String date, String availableSeats, int profileImage) {
        this.ownerName = ownerName;
        this.price = price;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.time = time;
        Rating = rating;
        this.date = date;
        this.availableSeats = availableSeats;
        this.profileImage = profileImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
