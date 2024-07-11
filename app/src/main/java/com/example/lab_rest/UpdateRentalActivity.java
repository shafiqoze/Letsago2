package com.example.lab_rest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRentalActivity extends AppCompatActivity {

    private EditText txtCustomerName;
    private EditText txtRemarks;
    private static TextView tvPickupdate;
    private static TextView tvReturndate;
    private static Date Pickupdate;
    private static Date Returndate;

    private RentalService rentalService;
    private Spinner carSpinner;
    private Spinner statusSpinner;
    private int carId;
    private String carName;
    private String carModel;
    private String status;
    private ArrayAdapter<String> carAdapter;
    private ArrayAdapter<String> statusAdapter;
    private List<Car> carList;
    private int bookingId; // Declare bookingId as a class member
    private String createdAt;

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private boolean isPickupDate;

        public DatePickerFragment(boolean isPickupDate) {
            this.isPickupDate = isPickupDate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Date selectedDate = new GregorianCalendar(year, month, day).getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            if (isPickupDate) {
                Pickupdate = selectedDate;
                tvPickupdate.setText(sdf.format(Pickupdate));
            } else {
                Returndate = selectedDate;
                tvReturndate.setText(sdf.format(Returndate));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rental);

        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtRemarks = findViewById(R.id.txtRemarks);
        tvPickupdate = findViewById(R.id.tvPickupdate);
        tvReturndate = findViewById(R.id.tvReturndate);
        carSpinner = findViewById(R.id.carSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);

        Pickupdate = new Date();
        Returndate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        tvPickupdate.setText(sdf.format(Pickupdate));
        tvReturndate.setText(sdf.format(Returndate));

        rentalService = ApiUtils.getRentalService();

        loadCarList();
        loadStatusList();

        // Get the booking details from the intent
        Intent intent = getIntent();
        bookingId = intent.getIntExtra("booking_id", 0); // Retrieve bookingId from the intent
        String customerName = intent.getStringExtra("customer_name");
        carId = intent.getIntExtra("car_id", 0);
        String pickupDate = intent.getStringExtra("pickup_date");
        String returnDate = intent.getStringExtra("return_date");
        String remarks = intent.getStringExtra("remarks");
        createdAt = intent.getStringExtra("created_at"); // Include createdAt
        status = intent.getStringExtra("status"); // Include status

        txtCustomerName.setText(customerName);
        txtRemarks.setText(remarks);
        tvPickupdate.setText(pickupDate);
        tvReturndate.setText(returnDate);

        // Set the selected car in the spinner
        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Car selectedCar = carList.get(position);
                carId = selectedCar.getId();
                carName = selectedCar.getName();
                carModel = selectedCar.getModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set the selected status in the spinner
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = statusSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void showPickupDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showReturnDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(false);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateRental(View v) {
        String customerName = txtCustomerName.getText().toString().trim();
        String remarks = txtRemarks.getText().toString().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String pickupDate = sdf.format(Pickupdate);
        String returnDate = sdf.format(Returndate);

        checkCarAvailability(customerName, carId, pickupDate, returnDate, remarks, status);
    }

    private void loadCarList() {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.getAllCars(apiKey).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList = response.body();
                    List<String> carNames = new ArrayList<>();
                    for (Car car : carList) {
                        carNames.add(car.getName());
                    }
                    carAdapter = new ArrayAdapter<>(UpdateRentalActivity.this, android.R.layout.simple_spinner_item, carNames);
                    carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carSpinner.setAdapter(carAdapter);

                    // Set the selected car
                    for (int i = 0; i < carList.size(); i++) {
                        if (carList.get(i).getId() == carId) {
                            carSpinner.setSelection(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(UpdateRentalActivity.this, "Failed to load car list: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("UpdateRentalActivity", "Failed to load car list: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(UpdateRentalActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateRentalActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void loadStatusList() {
        List<String> statuses = new ArrayList<>();
        statuses.add("pending");
        statuses.add("approved");
        statuses.add("rejected");

        statusAdapter = new ArrayAdapter<>(UpdateRentalActivity.this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Set the selected status
        if (status != null) {
            int spinnerPosition = statusAdapter.getPosition(status);
            statusSpinner.setSelection(spinnerPosition);
        }
    }

    private void checkCarAvailability(String customerName, int carId, String pickupDate, String returnDate, String remarks, String status) {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.getAllBookings(apiKey).enqueue(new Callback<List<Bookings>>() {
            @Override
            public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bookings> bookings = response.body();
                    boolean isAvailable = true;
                    for (Bookings booking : bookings) {
                        if (booking.getCarId() == carId &&
                                !(booking.getPickupDate().compareTo(returnDate) >= 0 ||
                                        booking.getReturnDate().compareTo(pickupDate) <= 0)) {
                            isAvailable = false;
                            break;
                        }
                    }
                    if (isAvailable) {
                        updateRental(bookingId, customerName, carId, pickupDate, returnDate, remarks, status, createdAt); // Include status and createdAt
                    } else {
                        Toast.makeText(UpdateRentalActivity.this, "Car is not available for the selected dates.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateRentalActivity.this, "Failed to check car availability: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("UpdateRentalActivity", "Failed to check car availability: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Bookings>> call, Throwable t) {
                Toast.makeText(UpdateRentalActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateRentalActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void updateRental(int bookingId, String customerName, int carId, String pickupDate, String returnDate, String remarks, String status, String createdAt) {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        // Log the data being sent
        Log.d("UpdateRentalActivity", "Booking ID: " + bookingId);
        Log.d("UpdateRentalActivity", "Customer Name: " + customerName);
        Log.d("UpdateRentalActivity", "Car ID: " + carId);
        Log.d("UpdateRentalActivity", "Pickup Date: " + pickupDate);
        Log.d("UpdateRentalActivity", "Return Date: " + returnDate);
        Log.d("UpdateRentalActivity", "Remarks: " + remarks);
        Log.d("UpdateRentalActivity", "Status: " + status);
        Log.d("UpdateRentalActivity", "Created At: " + createdAt);

        // Create the booking object to send
        Bookings booking = new Bookings();
        booking.setId(bookingId);
        booking.setCustName(customerName);
        booking.setCarId(carId);
        booking.setPickupDate(pickupDate);
        booking.setReturnDate(returnDate);
        booking.setRemarks(remarks);
        booking.setStatus(status);

        // Convert booking to JSON to log the payload
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(booking);
        Log.d("UpdateRentalActivity", "Payload: " + jsonPayload);

        rentalService.updateBookings(apiKey, bookingId, customerName, carId, pickupDate, returnDate, remarks, status, createdAt).enqueue(new Callback<Bookings>() {
            @Override
            public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UpdateRentalActivity.this, "Booking updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateRentalActivity.this, "Failed to update booking: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("UpdateRentalActivity", "Failed to update booking: " + response.message());
                    try {
                        Log.e("UpdateRentalActivity", "Response error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Bookings> call, Throwable t) {
                Toast.makeText(UpdateRentalActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateRentalActivity", "Error: " + t.getMessage());
            }
        });
    }


}
