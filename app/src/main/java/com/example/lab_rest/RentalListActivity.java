package com.example.lab_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.RentalAdapter;
import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalListActivity extends AppCompatActivity implements RentalAdapter.OnBookingClickListener {

    private RentalService rentalService;
    private RecyclerView rvRentalList;
    private RentalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        rvRentalList = findViewById(R.id.rvRentalList);
        registerForContextMenu(rvRentalList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRentalList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRentalList.getContext(), layoutManager.getOrientation());
        rvRentalList.addItemDecoration(dividerItemDecoration);

        adapter = new RentalAdapter(this, new ArrayList<>(), this); // Initialize with an empty list
        rvRentalList.setAdapter(adapter);

        updateRecyclerView();
    }

    private void updateRecyclerView() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();
        String token = user.getToken();

        rentalService = ApiUtils.getRentalService();
        rentalService.getAllBookings(token).enqueue(new Callback<List<Bookings>>() {
            @Override
            public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                Log.d("RentalListActivity", "Response: " + response.raw().toString());

                if (response.isSuccessful()) {
                    List<Bookings> bookings = response.body();
                    adapter.setData(bookings); // Update adapter data
                } else {
                    Toast.makeText(RentalListActivity.this, "Failed to fetch bookings: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("RentalListActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Bookings>> call, Throwable t) {
                Toast.makeText(RentalListActivity.this, "Error connecting to the server: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("RentalListActivity", "Error: " + t.getMessage(), t);
            }
        });
    }

    @Override
    public void onUpdateClick(Bookings booking) {
        Intent intent = new Intent(this, UpdateRentalActivity.class);
        intent.putExtra("booking_id", booking.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Bookings booking) {
        showDeleteConfirmationDialog(booking);
    }

    private void showDeleteConfirmationDialog(final Bookings booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteBookings(booking);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void doDeleteBookings(Bookings selectedBooking) {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();

        rentalService = ApiUtils.getRentalService();
        Call<DeleteResponse> call = rentalService.deleteBookings(user.getToken(), selectedBooking.getId());

        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    displayAlert("Booking successfully deleted");
                    updateRecyclerView(); // Refresh list after deletion
                } else {
                    Toast.makeText(RentalListActivity.this, "Failed to delete booking: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("RentalListActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(RentalListActivity.this, "Error deleting booking: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("RentalListActivity", "Error: " + t.getMessage(), t);
            }
        });
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rent_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getSelectedPosition();
        Bookings selectedBooking = adapter.getItem(position);

        if (item.getItemId() == R.id.menu_details) {
            // View details action
            doViewDetails(selectedBooking);
        } else if (item.getItemId() == R.id.menu_update) {
            // Update action
            doUpdateBookings(selectedBooking);
        } else if (item.getItemId() == R.id.menu_delete) {
            // Delete action
            doDeleteBookings(selectedBooking);
        }

        return super.onContextItemSelected(item);
    }

    private void doUpdateBookings(Bookings selectedBooking) {
        Intent intent = new Intent(this, UpdateRentalActivity.class);
        intent.putExtra("booking_id", selectedBooking.getId());
        startActivity(intent);
    }

    private void doViewDetails(Bookings selectedBooking) {
        Intent intent = new Intent(this, RentalDetailsActivity.class);
        intent.putExtra("booking_id", selectedBooking.getId());
        startActivity(intent);
    }

    public void floatingAddRentalClicked(View view) {
        Intent intent = new Intent(this, NewRentalActivity.class);
        startActivity(intent);
    }
}
