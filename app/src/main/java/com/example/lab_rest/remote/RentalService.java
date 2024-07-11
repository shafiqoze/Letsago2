package com.example.lab_rest.remote;

import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RentalService {

    // Get all bookings ordered by creation date in descending order
    @GET("bookings/?order=created_at&orderType=desc")
    Call<List<Bookings>> getAllBookings(@Header("api-key") String api_key);

    // Get details of a specific booking by ID
    @GET("bookings/{id}")
    Call<Bookings> getBookings(@Header("api-key") String api_key, @Path("id") int id);

    // Add a new booking
    @FormUrlEncoded
    @POST("bookings")
    Call<Bookings> addBookings(@Header("api-key") String apiKey,
                               @Field("cust_name") String custName,
                               @Field("car_id") int carId,
                               @Field("pickup_date") String pickupDate,
                               @Field("return_date") String returnDate,
                               @Field("remarks") String remarks);

    // Delete a booking by ID
    @DELETE("bookings/{id}")
    Call<DeleteResponse> deleteBookings(@Header("api-key") String apiKey, @Path("id") int id);

    // Update a booking by ID
    @FormUrlEncoded
    @POST("bookings/{id}")
    Call<Bookings> updateBookings(@Header("api-key") String apiKey, @Path("id") int id,
                                  @Field("cust_name") String customerName,
                                  @Field("car_id") int carId,
                                  @Field("pickup_date") String pickupDate,
                                  @Field("return_date") String returnDate,
                                  @Field("remarks") String remarks,
                                  @Field("status") String status,
                                  @Field("created_at") String createdAt);

    // Add a new car
    @FormUrlEncoded
    @POST("cars/add")
    Call<Car> addCar(@Header("api-key") String apiKey,
                     @Field("name") String name,
                     @Field("model") String model);

    // Get details of a specific car by ID
    @GET("cars/{id}")
    Call<Car> getCarDetails(@Header("api-key") String api_key, @Path("id") int id);

    // Get all cars
    @GET("cars")
    Call<List<Car>> getAllCars(@Header("api-key") String api_key);

    // Check car availability for a specific period
    @GET("bookings/availability")
    Call<Boolean> checkCarAvailability(@Header("api-key") String apiKey,
                                       @Query("car_id") int carId,
                                       @Query("pickup_date") String pickupDate,
                                       @Query("return_date") String returnDate);
}
