package com.example.lab_rest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {

    private EditText etCarName;
    private EditText etCarModel;
    private Button btnAddCar;
    private RentalService rentalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        etCarName = findViewById(R.id.etCarName);
        etCarModel = findViewById(R.id.etCarModel);
        btnAddCar = findViewById(R.id.btnAddCar);

        rentalService = ApiUtils.getRentalService();

        btnAddCar.setOnClickListener(v -> {
            String carName = etCarName.getText().toString().trim();
            String carModel = etCarModel.getText().toString().trim();

            if (!carName.isEmpty() && !carModel.isEmpty()) {
                addNewCar(carName, carModel);
            } else {
                Toast.makeText(AddCarActivity.this, "Please enter car details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewCar(String carName, String carModel) {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.addCar(apiKey, carName, carModel).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddCarActivity.this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish activity after successful addition
                } else {
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(AddCarActivity.this, "Failed to add car: " + errorBody, Toast.LENGTH_SHORT).show();
                            Log.e("AddCarActivity", "Response Error Body: " + errorBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(AddCarActivity.this, "Failed to add car: " + response.message(), Toast.LENGTH_SHORT).show();
                        Log.e("AddCarActivity", "Failed to add car: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(AddCarActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddCarActivity", "Error adding car", t);
            }
        });
    }
}
