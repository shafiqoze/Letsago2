package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RentalService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarListActivity extends AppCompatActivity {

    private RentalService rentalService;
    private List<Car> carList;
    private ListView carListView;
    private ArrayAdapter<String> carAdapter;
    private List<String> carNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        carListView = findViewById(R.id.listViewCars);

        rentalService = ApiUtils.getRentalService();
        loadCarList();

        carListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car selectedCar = carList.get(position);
                Intent intent = new Intent(CarListActivity.this, NewRentalActivity.class);
                intent.putExtra("carId", selectedCar.getId());
                intent.putExtra("carName", selectedCar.getName());
                intent.putExtra("carModel", selectedCar.getModel());
                startActivity(intent);
            }
        });
    }

    private void loadCarList() {
        SharedPrefManager spm = SharedPrefManager.getInstance(getApplicationContext());
        String apiKey = spm.getUser().getToken();

        rentalService.getAllCars(apiKey).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList = response.body();
                    carNames = new ArrayList<>();
                    for (Car car : carList) {
                        carNames.add(car.getName());
                    }
                    carAdapter = new ArrayAdapter<>(CarListActivity.this, android.R.layout.simple_list_item_1, carNames);
                    carListView.setAdapter(carAdapter);
                } else {
                    Toast.makeText(CarListActivity.this, "Failed to load car list: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("CarListActivity", "Failed to load car list: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(CarListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CarListActivity", "Error: " + t.getMessage());
            }
        });
    }
}
