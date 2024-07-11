package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.sharedpref.SharedPrefManager;

public class CustomerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
    }

    public void onBookCarClicked(View view) {
        Intent intent = new Intent(this, CarListActivity.class);
        startActivity(intent);
    }

    public void onCheckStatusClicked(View view) {
        Intent intent = new Intent(this, BookingStatusActivity.class);
        startActivity(intent);
    }

    public void onLogoutClicked(View view) {
        SharedPrefManager.getInstance(this).logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "You have successfully logged out.", Toast.LENGTH_LONG).show();
    }
}
