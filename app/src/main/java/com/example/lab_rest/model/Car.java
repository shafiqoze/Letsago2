package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class Car {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    public Car() {
    }

    public Car(int id, String name, String model) {
        this.id = id;
        this.name = name;
        this.model = model;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    @Override
    public String toString() {
        return name;
    }
}
