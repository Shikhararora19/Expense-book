package com.example.shikhar3_expbook;

import android.os.Parcel;
import android.os.Parcelable;

public class expense implements Parcelable {
    private String date;
    private String name;
    private float charge;
    private String comment;

    // Constructor to initialize an expense object
    public expense(String date, String name, float charge, String comment) {
        this.date = date;
        this.name = name;
        this.charge = charge;
        this.comment = comment;
    }

    // Getters and setters for expense fields
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Parcelable implementation for passing expense objects between activities
    //Citation: https://medium.com/techmacademy/how-to-implement-and-use-a-parcelable-class-in-android-part-1-28cca73fc2d1
    protected expense(Parcel in) {
        date = in.readString();
        name = in.readString();
        charge = in.readFloat();
        comment = in.readString();
    }

    public static final Creator<expense> CREATOR = new Creator<expense>() {
        @Override
        public expense createFromParcel(Parcel in) {
            return new expense(in);
        }

        @Override
        public expense[] newArray(int size) {
            return new expense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(name);
        dest.writeFloat(charge);
        dest.writeString(comment);
    }

    // Generate a string representation of an expense
    public String toString() {
        return "Name: " + name + ", Amount: " + charge +
                ", Month Started: " + date + ", Comment: " + comment;
    }
}
