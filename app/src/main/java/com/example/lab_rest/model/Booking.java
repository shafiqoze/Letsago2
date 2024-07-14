package com.example.lab_rest.model;

import java.util.Date;

public class Booking {

    private int BookingID;
    private String PickupDate;
    private String ReturnDate;
    private String Status;
    private double Price;
    private int id;
    private int CarID;

    public User user;
    public Car car;

    public Booking() {
    }

    public Booking(int bookingID, String pickup_date, String return_date, String booking_status, double totalPrice, int user_id , int car_id) {
        this.BookingID = bookingID;
        this.PickupDate = pickup_date;
        this.ReturnDate = return_date;
        this.Status = booking_status;
        this.Price = totalPrice;
        this.id = user_id;
        this.CarID = car_id;
    }

    public int getBookingID() {
        return BookingID;
    }

    public void setBookingID(int bookingID) {
        BookingID = bookingID;
    }

    public String getPickupDate() {
        return PickupDate;
    }

    public void setPickupDate(String pickupDate) {
        PickupDate = pickupDate;
    }

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(String returnDate) {
        ReturnDate = returnDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarID() {
        return CarID;
    }

    public void setCarID(int carID) {
        CarID = carID;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + BookingID +
                ", pickup_date=" + PickupDate +
                ", return_date=" + ReturnDate +
                ", booking_status='" + Status + '\'' +
                ", totalPrice=" + Price +
                ", user_id=" + id +
                ", car_id=" + CarID +
                '}';
    }
}
