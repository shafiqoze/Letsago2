package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalDetailsActivity extends AppCompatActivity {

    private RentalService rentalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_details);

        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        TextView tvCarName = findViewById(R.id.tvCarName);
        TextView tvPickupdate = findViewById(R.id.tvPickupdate);
        TextView tvReturndate = findViewById(R.id.tvReturndate);
        TextView tvRemarks = findViewById(R.id.tvRemarks);
        TextView tvStatus = findViewById(R.id.tvStatus); // Add this line

        Intent intent = getIntent();
        int bookingId = intent.getIntExtra("bookingId", -1);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        String token = spm.getUser().getToken();

        rentalService = ApiUtils.getRentalService();

        rentalService.getBookings(token, bookingId).enqueue(new Callback<Bookings>() {
            @Override
            public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                if (response.isSuccessful()) {
                    Bookings booking = response.body();
                    tvCustomerName.setText(booking.getCustName());
                    tvCarName.setText("Car ID: " + booking.getCarId());
                    tvPickupdate.setText("Pickup Date: " + booking.getPickupDate());
                    tvReturndate.setText("Return Date: " + booking.getReturnDate());
                    tvRemarks.setText("Remarks: " + booking.getRemarks());
                    tvStatus.setText("Status: " + booking.getStatus()); // Add this line
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(RentalDetailsActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("RentalDetailsActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Bookings> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("RentalDetailsActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
