package com.example.lab_rest;

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

public class NewCarActivity extends AppCompatActivity {

    private EditText txTCarID;
    private EditText txtCarBrand;
    private EditText txtCarName;
    private EditText txtCarPlateNo;
    private EditText txtCarPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get view objects references
        txTCarID = findViewById(R.id.txtCarID);
        txtCarBrand = findViewById(R.id.txtCarBrand);
        txtCarName = findViewById(R.id.txtCarName);
        txtCarPlateNo = findViewById(R.id.txtCarPlateNo);
        txtCarPrice = findViewById(R.id.txtCarPrice);
    }

    /**
     * Called when Add Car button is clicked
     * @param v
     */
    public void addNewCar(View v) {
        // get values in form
        String carIdString = txTCarID.getText().toString();
        int CarID = Integer.parseInt(carIdString);
        String CarBrand = txtCarBrand.getText().toString();
        String CarName = txtCarName.getText().toString();
        String CarPlateNo = txtCarPlateNo.getText().toString();
        String CarPrice = txtCarPrice.getText().toString();

        Log.d("NewCarActivity", "Car details: ID=" + CarID + ", Brand=" + CarBrand + ", Name=" + CarName +
                ", PlateNo=" + CarPlateNo + ", Price=" + CarPrice);

        // get user info from SharedPreferences
        SharePrefManager spm = new SharePrefManager(getApplicationContext());
        User user = spm.getUser();


        // send request to add new car to the REST API
        CarService carService = ApiUtils.getCarService();
        Call<Car> call = carService.addCar(user.getToken(), CarID, CarBrand, CarName, CarPlateNo, CarPrice);

        // execute
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 201) {
                    // car added successfully
                    Car addedCar = response.body();
                    // display message
                    Toast.makeText(getApplicationContext(),
                            addedCar.getCarID() + " added successfully.",
                            Toast.LENGTH_LONG).show();

                    // end this activity and forward user to CarListActivity
                    Intent intent = new Intent(getApplicationContext(), CarListActivity.class);
                    startActivity(intent);
                    finish();
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
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error [" + t.getMessage() + "]",
                        Toast.LENGTH_LONG).show();
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
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
}
