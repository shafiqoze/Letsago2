package com.example.lab_rest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingStatusActivity extends AppCompatActivity {

    private TextView tvBookingDetails;
    private RentalService rentalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);

        tvBookingDetails = findViewById(R.id.tvBookingDetails);
        rentalService = ApiUtils.getRentalService();

        loadBookingDetails();
    }

    private void loadBookingDetails() {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.getAllBookings(apiKey).enqueue(new Callback<List<Bookings>>() {
            @Override
            public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bookings> bookings = response.body();
                    StringBuilder detailsBuilder = new StringBuilder();

                    for (Bookings booking : bookings) {
                        detailsBuilder.append("Booking ID: ").append(booking.getId()).append("\n");
                        detailsBuilder.append("Customer Name: ").append(booking.getCustName()).append("\n");
                        detailsBuilder.append("Car: ").append(booking.getCarId()).append("\n");
                        detailsBuilder.append("Pickup Date: ").append(booking.getPickupDate()).append("\n");
                        detailsBuilder.append("Return Date: ").append(booking.getReturnDate()).append("\n");
                        detailsBuilder.append("Remarks: ").append(booking.getRemarks()).append("\n");
                        detailsBuilder.append("Status: ").append(booking.getStatus()).append("\n\n");
                    }

                    tvBookingDetails.setText(detailsBuilder.toString());
                } else {
                    Toast.makeText(BookingStatusActivity.this, "Failed to load booking details: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("BookingStatusActivity", "Failed to load booking details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Bookings>> call, Throwable t) {
                Toast.makeText(BookingStatusActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BookingStatusActivity", "Error: " + t.getMessage());
            }
        });
    }
}
