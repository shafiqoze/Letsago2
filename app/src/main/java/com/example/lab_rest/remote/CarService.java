package com.example.lab_rest.remote;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarService {

    @GET("Cars")
    Call<List<Car>> getAllCars(@Header("api-key") String api_key);

    @FormUrlEncoded
    @POST("Cars")
    Call<Car> addCar(
            @Header("api-key") String apiKey,
            @Field("CarBrand") String CarBrand, @Field("CarName") String CarName,
            @Field("CarPlateNo") String CarPlateNo, @Field("CarPrice") String CarPrice

    );

    @GET("Cars/{id}")
    Call<Car> getCar(
            @Header("api-key") String token,
            @Path("id") int id
    );


    @PUT("Cars/{id}")
        Call<Car> updateCar(
                @Header("Authorization") String token,
                @Body Car car
    );


    @DELETE("Cars/{id}")
    Call<DeleteResponse> deleteCar(
            @Header(("api_key")) String apikey,
            @Path("id") int id
    );

}