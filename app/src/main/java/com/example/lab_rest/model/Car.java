package com.example.lab_rest.model;

public class Car {
    private int CarID;
    private String CarName;
    private String CarBrand;
    private String CarPrice;
    private String CarPlateNo;

    public Car() {
    }

    public Car(int carID, String carName, String carBrand, String carPrice, String carPlateNo) {
        CarID = carID;
        CarName = carName;
        CarBrand = carBrand;
        CarPrice = carPrice;
        CarPlateNo = carPlateNo;
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

    @Override
    public String toString() {
        return "Car{" +
                "CarID=" + CarID +
                ", CarName='" + CarName + '\'' +
                ", CarBrand='" + CarBrand + '\'' +
                ", CarPrice='" + CarPrice + '\'' +
                ", CarPlateNo='" + CarPlateNo + '\'' +
                '}';
    }
}
