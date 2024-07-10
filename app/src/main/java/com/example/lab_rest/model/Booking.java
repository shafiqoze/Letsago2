package com.example.lab_rest.model;

public class Booking {
    private int BookingID;
    private String PickupDate;
    private String ReturnDate;
    private String Status;
    private String Price;
    private int id;
    private int CarID;


    public Booking() {
    }

    public Booking(int bookingID, String pickupDate, String returnDate, String status, String price, int id, int carID) {
        BookingID = bookingID;
        PickupDate = pickupDate;
        ReturnDate = returnDate;
        Status = status;
        Price = price;
        this.id = id;
        CarID = carID;
    }

    public int getId() {
        return id;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setCarID(int carID) {
        CarID = carID;
    }


    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "Booking id=" + BookingID +
                ", Price='" + Price + '\'' +
                ", Pickup='" + PickupDate + '\'' +
                ", Return='" + ReturnDate + '\'' +
                ", Status='" + Status + '\'' +
                ", UserID='" + id + '\'' +
                ", CarID='" + CarID + '\''
                ;
    }
}