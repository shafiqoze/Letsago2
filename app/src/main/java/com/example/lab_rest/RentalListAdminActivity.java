package com.example.lab_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.RentalAdapter;
import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalListAdminActivity extends AppCompatActivity implements RentalAdapter.OnBookingClickListener {

    private RecyclerView recyclerView;
    private RentalAdapter rentalAdapter;
    private List<Bookings> bookingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_admin);

        recyclerView = findViewById(R.id.rvRentalList);
        bookingsList = new ArrayList<>();
        rentalAdapter = new RentalAdapter(this, bookingsList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rentalAdapter);

        loadBookings();
    }

    private void loadBookings() {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();
        RentalService rentalService = ApiUtils.getRentalService();

        rentalService.getAllBookings(apiKey).enqueue(new Callback<List<Bookings>>() {
            @Override
            public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bookings> bookings = response.body();
                    rentalAdapter.setData(bookings); // Update adapter data
                } else {
                    Toast.makeText(RentalListAdminActivity.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bookings>> call, Throwable t) {
                Toast.makeText(RentalListAdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdateClick(Bookings booking) {
        // Navigate to UpdateRentalActivity and pass booking details via intent
        Intent intent = new Intent(RentalListAdminActivity.this, UpdateRentalActivity.class);
        intent.putExtra("booking_id", booking.getId());
        intent.putExtra("customer_name", booking.getCustName());
        intent.putExtra("car_id", booking.getCarId());
        intent.putExtra("pickup_date", booking.getPickupDate());
        intent.putExtra("return_date", booking.getReturnDate());
        intent.putExtra("remarks", booking.getRemarks());
        intent.putExtra("status", booking.getStatus());  // Pass the status as well
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Bookings booking) {
        confirmDeleteBooking(booking);
    }

    private void confirmDeleteBooking(Bookings selectedBooking) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteBooking(selectedBooking);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void doDeleteBooking(Bookings selectedBooking) {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();
        RentalService rentalService = ApiUtils.getRentalService();

        rentalService.deleteBookings(apiKey, selectedBooking.getId()).enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingsList.remove(selectedBooking);
                    rentalAdapter.notifyDataSetChanged();
                    Toast.makeText(RentalListAdminActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RentalListAdminActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(RentalListAdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
