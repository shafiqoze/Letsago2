package com.example.lab_rest.remote;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarService {

    @GET("Cars")
    Call<List<Car>> getAllCars(@Header("api-key") String api_key);

    @POST("Cars")
    Call<Car> addCar(
            @Header("Authorization") String token,
            @Body Car car
    );

    @GET("Cars/{id}")
    Call<Car> getCar(
            @Header("Authorization") String token,
            @Path("id") int id
    );


    @PUT("Cars/{id}")
        Call<Car> updateCar(
                @Header("Authorization") String token,
                @Body Car car
    );


    @DELETE("Car/{id}")
    Call<DeleteResponse> deleteCar(
            @Header("Authorization") String token,
            @Path("id") int id
    );

}