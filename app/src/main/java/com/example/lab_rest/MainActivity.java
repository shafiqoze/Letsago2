package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bookCar(View view) {
        Intent intent = new Intent(MainActivity.this, CarListActivity.class);
        startActivity(intent);
    }
}
