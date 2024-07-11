package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class Bookings {

    @SerializedName("id")
    private int id;

    @SerializedName("cust_name")
    private String custName;

    @SerializedName("car_id")
    private int carId;

    @SerializedName("pickup_date")
    private String pickupDate;

    @SerializedName("return_date")
    private String returnDate;

    @SerializedName("remarks")
    private String remarks;

    // New field for car name
    @SerializedName("car_name")
    private String carName;

    // New field for status
    @SerializedName("status")
    private String status;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Getter and Setter for car name
    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
