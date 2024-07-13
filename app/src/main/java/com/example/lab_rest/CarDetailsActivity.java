package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarDetailsActivity extends AppCompatActivity {
    private CarService carService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("CarDetailsActivity", "onCreate called");

        // Retrieve car details based on selected id
        Intent intent = getIntent();
        int carId = intent.getIntExtra("car_id", -1);
        Log.d("CarDetailsActivity", "Received car_id: " + carId);

        // Get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();
        Log.d("CarDetailsActivity", "User token: " + token);

        // Get car service instance
        carService = ApiUtils.getCarService();

        // Execute the API query to get car details
        carService.getCar(token, carId).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success

                    // get car object from response
                    Car car = response.body();

                    // Get references to the view elements
                    TextView tvCarBrand = findViewById(R.id.tvCarBrand);
                    TextView tvCarName = findViewById(R.id.tvCarName);
                    TextView tvCarPrice = findViewById(R.id.tvCarPrice);

                    // Set values
                    tvCarBrand.setText(car.getCarBrand());
                    tvCarName.setText(car.getCarName());
                    tvCarPrice.setText(car.getCarPrice());

                } else if (response.code() == 401) {
                    // unauthorized error. invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    // server return other error
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting", Toast.LENGTH_LONG).show();
                Log.e("CarDetailsActivity", "Error: " + t.getMessage());
            }
        });
    }

    public void clearSessionAndRedirect() {
        Log.d("CarDetailsActivity", "Clearing session and redirecting to login");
        // Clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // Terminate this activity
        finish();

        // Forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}