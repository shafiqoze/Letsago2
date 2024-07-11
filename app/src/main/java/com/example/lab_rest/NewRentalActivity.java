package com.example.lab_rest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.lab_rest.model.Bookings;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRentalActivity extends AppCompatActivity {

    private EditText txtCustomerName;
    private EditText txtRemarks;
    private static TextView tvPickupdate;
    private static TextView tvReturndate;
    private static Date pickupDate;
    private static Date returnDate;

    private RentalService rentalService;
    private int carId;
    private String carName;
    private String carModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rental);

        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtRemarks = findViewById(R.id.txtRemarks);
        tvPickupdate = findViewById(R.id.tvPickupdate);
        tvReturndate = findViewById(R.id.tvReturndate);

        rentalService = ApiUtils.getRentalService();

        Intent intent = getIntent();
        carId = intent.getIntExtra("carId", 0);
        carName = intent.getStringExtra("carName");
        carModel = intent.getStringExtra("carModel");

        TextView carDetails = findViewById(R.id.tvCarName);
        carDetails.setText("Car: " + carName + " - " + carModel);
    }

    public void showPickupDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showReturnDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(false);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addNewRental(View v) {
        String custName = txtCustomerName.getText().toString().trim();
        String remarks = txtRemarks.getText().toString().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String pickupDateStr = sdf.format(pickupDate);
        String returnDateStr = sdf.format(returnDate);

        checkCarAvailability(custName, carId, pickupDateStr, returnDateStr, remarks);
    }

    private void checkCarAvailability(String custName, int carId, String pickupDate, String returnDate, String remarks) {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.getAllBookings(apiKey).enqueue(new Callback<List<Bookings>>() {
            @Override
            public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bookings> bookingsList = response.body();

                    for (Bookings booking : bookingsList) {
                        if (booking.getCarId() == carId &&
                                ((pickupDate.compareTo(booking.getPickupDate()) >= 0 && pickupDate.compareTo(booking.getReturnDate()) <= 0) ||
                                        (returnDate.compareTo(booking.getPickupDate()) >= 0 && returnDate.compareTo(booking.getReturnDate()) <= 0))) {
                            Toast.makeText(NewRentalActivity.this, "Car is not available for the selected period", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    addBooking(custName, carId, pickupDate, returnDate, remarks);
                } else {
                    Toast.makeText(NewRentalActivity.this, "Failed to check availability", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bookings>> call, Throwable t) {
                Toast.makeText(NewRentalActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBooking(String custName, int carId, String pickupDate, String returnDate, String remarks) {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.addBookings(apiKey, custName, carId, pickupDate, returnDate, remarks).enqueue(new Callback<Bookings>() {
            @Override
            public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(NewRentalActivity.this, "Booking added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NewRentalActivity.this, "Failed to add booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bookings> call, Throwable t) {
                Toast.makeText(NewRentalActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private boolean isPickupDate;

        public DatePickerFragment() {
        }

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
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date date = cal.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String dateString = sdf.format(date);

            if (isPickupDate) {
                pickupDate = date;
                tvPickupdate.setText(dateString);
            } else {
                returnDate = date;
                tvReturndate.setText(dateString);
            }
        }
    }
}
