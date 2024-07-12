package com.example.lab_rest.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BookingService {

    @GET("booking")
    Call<List<Booking>> getAllBooking(@Header("api-key") String api_key);

    @GET("booking/{BookingID}")
    Call<Booking> getBooking(@Header("api-key") String api_key, @Path("BookingID") int BookingID);
}