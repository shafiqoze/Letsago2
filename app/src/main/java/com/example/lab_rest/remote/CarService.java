package com.example.lab_rest.remote;

import com.example.lab_rest.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CarService {

    @GET("Cars")
    Call<List<Car>> getAllCars(@Header("api-key") String api_key);

}