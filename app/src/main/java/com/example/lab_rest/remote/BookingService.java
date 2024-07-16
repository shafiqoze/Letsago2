package com.example.lab_rest.remote;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.Booking;


import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookingService {
    @GET("booking/?order=pickupDate&orderType=desc")
    Call<List<Booking>> getAllBooking(@Header("api-key") String api_key);

    @GET("booking/{BookingID}")
    Call<Booking> getBooking(@Header("api-key") String api_key, @Path("BookingID") int BookingID);

    @FormUrlEncoded
    @POST("booking")
    Call<Booking> addBooking(@Header("api-key") String apiKey,
                             @Field("pickupDate") String pickup_date,
                             @Field("returnDate") String return_date,
                             @Field("status") String booking_status,
                             @Field("price") double totalPrice,
                             @Field("user_id") int user_id,
                             @Field("car_id") int car_id );

    @DELETE("booking/{BookingID}")
    Call<DeleteResponse> deleteBooking(@Header ("api-key") String apiKey, @Path("BookingID") int BookingID);

    @FormUrlEncoded
    @POST("booking/{BookingID}")
    Call<Car> updateBooking(@Header ("api-key") String apiKey,
                            @Field("bookingID") int bookingID,
                            @Field("pickup_date") String pickup_date,
                            @Field("return_date") String return_date,
                            @Field("pickup_location") String pickup_location,
                            @Field("return_location") String return_location,
                            @Field("booking_status") String booking_status,
                            @Field("totalPrice") double totalPrice,
                            @Field("user_id") int user_id,
                            @Field("admin_id") int admin_id,
                            @Field("car_id") int car_id);

    @FormUrlEncoded
    @POST("booking/updateStatus/{bookingID}")
    Call<Booking> updateBookingStatus(
            @Header("api-key") String apiKey,
            @Path("bookingID") int bookingID,
            @Field("booking_status") String booking_status,
            @Field("message") String message);

}
