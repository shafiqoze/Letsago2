package com.example.lab_rest.model;

public class Car {
    private int CarID;
    private String CarName;
    private String CarBrand;
    private String CarPrice;
    private String CarPlateNo;
    private char Status;

    public Car() {
    }

    public Car(int carID, String carName, String carBrand, String carPrice, String carPlateNo, char status) {
        CarID = carID;
        CarName = carName;
        CarBrand = carBrand;
        CarPrice = carPrice;
        CarPlateNo = carPlateNo;
        Status = status;
    }

    public int getCarID() {
        return CarID;
    }

    public void setCarID(int carID) {
        CarID = carID;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public String getCarBrand() {
        return CarBrand;
    }

    public void setCarBrand(String carBrand) {
        CarBrand = carBrand;
    }

    public String getCarPrice() {
        return CarPrice;
    }

    public void setCarPrice(String carPrice) {
        CarPrice = carPrice;
    }

    public String getCarPlateNo() {
        return CarPlateNo;
    }

    public void setCarPlateNo(String carPlateNo) {
        CarPlateNo = carPlateNo;
    }

    public char getStatus() {
        return Status;
    }

    public void setStatus(char status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + CarID +
                ", name='" + CarName + '\'' +
                ", brand='" + CarBrand + '\'' +
                ", price='" + CarPrice + '\'' +
                ", plate no='" + CarPlateNo + '\'' +
                ", status='" + Status + '\'' +
                '}';
    }
}
