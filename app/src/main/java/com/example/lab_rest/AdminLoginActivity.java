package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                authenticateAdmin(username, password);
            }
        });
    }

    private void authenticateAdmin(String username, String password) {
        UserService userService = ApiUtils.getUserService();
        Call<User> call = userService.adminLogin(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    if ("admin".equals(user.getRole())) {
                        sharedPrefManager.saveUser(user);
                        Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Not an admin user", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AdminLoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
