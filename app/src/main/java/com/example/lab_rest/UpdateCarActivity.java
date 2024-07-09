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
import com.example.lab_rest.sharedpref.SharePrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCarActivity extends AppCompatActivity {

    // form fields
    private EditText txTCarID;
    private EditText txtCarBrand;
    private EditText txtCarName;
    private EditText txtCarPlateNo;
    private EditText txtCarPrice;
    private EditText txtStatus;


    private Car car;  // current book to be updated



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

        // retrieve book id from intent
        // get book id sent by BookListActivity, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("car_id", -1);

        // get references to the form fields in layout
        txTCarID = findViewById(R.id.txtCarID);
        txtCarBrand = findViewById(R.id.txtCarBrand);
        txtCarName = findViewById(R.id.txtCarName);
        txtCarPlateNo = findViewById(R.id.txtCarPlateNo);
        txtCarPrice = findViewById(R.id.txtCarPrice);
        txtStatus = findViewById(R.id.txtStatus);

        // retrieve book info from database using the book id
        // get user info from SharedPreferences
        SharePrefManager spm = new SharePrefManager(getApplicationContext());
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
                    txTCarID.setText(car.getCarID());
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
        SharePrefManager spm = new SharePrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
    /**
     * Update book info in database when the user click Update Book button
     * @param view
     */
    public void updateCar(View view) {
        // get values in form
        String carIdString = txTCarID.getText().toString();
        int CarID = Integer.parseInt(carIdString);
        String CarBrand = txtCarBrand.getText().toString();
        String CarName = txtCarName.getText().toString();
        String CarPlateNo = txtCarPlateNo.getText().toString();
        String CarPrice = txtCarPrice.getText().toString();
        char Status = txtStatus.getText().toString().charAt(0);

        Log.d("MyApp:", "Old Car info: " + car.toString());

        // update the book object retrieved in when populating the form with the new data.
        // update all fields excluding the id
        car.setCarID(CarID);
        car.setCarBrand(CarBrand);
        car.setCarPrice(CarPrice);
        car.setCarPlateNo(CarPlateNo);
        car.setCarName(CarName);
        car.setStatus(Status);

        Log.d("MyApp:", "New Book info: " + car.toString());

        // get user info from SharedPreferences
        SharePrefManager spm = new SharePrefManager(getApplicationContext());
        User user = spm.getUser();

        // create Car object
        Car updateCar = new Car(CarID, CarBrand, CarName, CarPlateNo, CarPrice, Status);

        // send request to update the book record to the REST API
        CarService carService = ApiUtils.getCarService();
        Call<Car> call = carService.addCar(user.getToken(),car);

        // execute
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

                // for debug purpose
                Log.d("MyApp:", "Update Request Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success code for update request
                    // get updated book object from response
                    Car updatedCar = response.body();

                    // display message
                    displayUpdateSuccess(updatedCar.getCarID() + " updated successfully.");


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