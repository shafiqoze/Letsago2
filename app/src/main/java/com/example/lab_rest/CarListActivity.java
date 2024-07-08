package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.CarAdapter;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.sharedpref.SharePrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarListActivity extends AppCompatActivity {

    private CarService carService;
    private RecyclerView rvCarList;
    private CarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        // get reference to the RecyclerView carList
        rvCarList = findViewById(R.id.rvCarList);

        if (rvCarList == null) {
            Log.e("MyApp:", "RecyclerView is null. Check the layout file for the correct ID.");
            return;
        }

        // get user info from SharedPreferences to get token value
        SharePrefManager spm = new SharePrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        // get car service instance
        carService = ApiUtils.getCarService();

        // execute the call. send the user token when sending the query
        carService.getAllCars(token).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // Get list of car objects from response
                    List<Car> cars = response.body();

                    if (cars == null || cars.isEmpty()) {
                        Log.e("MyApp:", "Car list is empty or null.");
                        Toast.makeText(getApplicationContext(), "No cars available", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Log the car list size
                    Log.d("MyApp:", "Car list size: " + cars.size());

                    // initialize adapter
                    adapter = new CarAdapter(getApplicationContext(), cars);;

                    // set adapter to the RecyclerView
                    rvCarList.setAdapter(adapter);

                    // set layout to recycler view
                    rvCarList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // add separator between item in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCarList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvCarList.addItemDecoration(dividerItemDecoration);
                } else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharePrefManager spm = new SharePrefManager(getApplicationContext());
        spm.logout();

        // terminate this CarListActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
