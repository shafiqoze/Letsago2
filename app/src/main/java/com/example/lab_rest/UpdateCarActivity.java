package com.example.lab_rest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class UpdateCarActivity extends AppCompatActivity {

    // form fields
    private EditText txtCarBrand;
    private EditText txtCarName;
    private EditText txtCarPlateNo;
    private EditText txtCarPrice;

    private Car car;  // current car to be updated



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // retrieve car id from intent
        // get car id sent by CarListActivity, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("car_id", -1);


        // get references to the form fields in layout
        txtCarBrand = findViewById(R.id.txtCarBrand);
        txtCarName = findViewById(R.id.txtCarName);
        txtCarPlateNo = findViewById(R.id.txtCarPlateNo);
        txtCarPrice = findViewById(R.id.txtCarPrice);

        // retrieve book info from database using the book id
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // get book service instance
        CarService carService = ApiUtils.getCarService();

        // execute the API query. send the token and book id
        carService.getCar(user.getToken(), id).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                // for debug purpose
                Log.d("MyApp:", "Update Form Populate Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success
                    // get book object from response
                    car = response.body();

                    // set values into forms
                    txtCarBrand.setText(car.getCarBrand());
                    txtCarName.setText(car.getCarName());
                    txtCarPlateNo.setText(car.getCarPlateNo());
                    txtCarPrice.setText(car.getCarPrice());
                }
                else if (response.code() == 401) {
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
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    /**
     * Update car info in database when the user click Update Book button
     * @param view
     */
    public void updateCar(View view) {
        // get values in form
        String CarBrand = txtCarBrand.getText().toString();
        String CarName = txtCarName.getText().toString();
        String CarPlateNo = txtCarPlateNo.getText().toString();
        String CarPrice = txtCarPrice.getText().toString();


        Log.d("MyApp:", "Old Car info: " + car.toString());

        // update the car object retrieved in when populating the form with the new data.
        // update all fields excluding the id
        car.setCarBrand(CarBrand);
        car.setCarName(CarName);
        car.setCarPlateNo(CarPlateNo);
        car.setCarPrice(CarPrice);

        Log.d("MyApp:", "New Car info: " + car.toString());

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // send request to update the book record to the REST API
        CarService bookService = ApiUtils.getCarService();
        Call<Car> call = bookService.updateCar(user.getToken(), car.getCarID(),car.getCarBrand(), car.getCarName(),
                car.getCarPlateNo(), car.getCarPrice());

        // execute
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

                // for debug purpose
                Log.d("MyApp:", "Update Request Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success code for update request
                    // get updated car object from response
                    Car updatedCar = response.body();

                    // display message
                    displayUpdateSuccess(updatedCar.getCarName() + " updated successfully.");


                }
                else if (response.code() == 401) {
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
                displayAlert("Error [" + t.getMessage() + "]");
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayUpdateSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // end this activity and forward user to BookListActivity
                        Intent intent = new Intent(getApplicationContext(), CarListActivity.class);
                        startActivity(intent);
                        finish();

                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}