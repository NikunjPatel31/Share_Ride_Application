package com.example.shareride.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String userID;
    String firstName, lastName, city, contact, DOB, gender, pincode, profilePic;

    public User(String userID,
                String firstName,
                String lastName,
                String city,
                String contact,
                String DOB,
                String gender,
                String pincode,
                String profilePic) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.contact = contact;
        this.DOB = DOB;
        this.gender = gender;
        this.pincode = pincode;
        this.profilePic = profilePic;
    }

    protected User(Parcel in) {
        userID = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        city = in.readString();
        contact = in.readString();
        DOB = in.readString();
        gender = in.readString();
        pincode = in.readString();
        profilePic = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(city);
        parcel.writeString(contact);
        parcel.writeString(DOB);
        parcel.writeString(gender);
        parcel.writeString(pincode);
        parcel.writeString(profilePic);
    }
}
