package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class AvailabilityResponse {
    @SerializedName("available")
    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
