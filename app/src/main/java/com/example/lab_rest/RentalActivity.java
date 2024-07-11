package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.RentalAdapter;
import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RentalAdapter rentalAdapter;
    private List<Bookings> bookingsList;
    private Button buttonAddCar;
    private Button buttonViewBookings;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        recyclerView = findViewById(R.id.recyclerView);
        buttonAddCar = findViewById(R.id.buttonAddCar);
        buttonViewBookings = findViewById(R.id.buttonViewBookings);
        buttonLogout = findViewById(R.id.buttonLogout);

        bookingsList = new ArrayList<>();
        rentalAdapter = new RentalAdapter(this, bookingsList, new RentalAdapter.OnBookingClickListener() {
            @Override
            public void onUpdateClick(Bookings booking) {
                // Handle update click
            }

            @Override
            public void onDeleteClick(Bookings booking) {
                // Handle delete click
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rentalAdapter);

        buttonAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentalActivity.this, AddCarActivity.class);
                startActivity(intent);
            }
        });

        buttonViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBookings();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Load bookings initially
        loadBookings();
    }

    private void loadBookings() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        String apiKey = spm.getUser().getToken();
        RentalService rentalService = ApiUtils.getRentalService();
        rentalService.getAllBookings(apiKey).enqueue(new Callback<List<Bookings>>() {
            @Override
            public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingsList.clear();
                    bookingsList.addAll(response.body());
                    rentalAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bookings>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        Intent intent = new Intent(RentalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
