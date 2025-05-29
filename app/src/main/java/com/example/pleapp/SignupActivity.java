package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    EditText userName, email, cfEmail, etPassword, etConfirmPassword, phoneNumber;
    Button btnCreateAccount;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        cfEmail = findViewById(R.id.cfEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        db = new DatabaseHelper(this);

        btnCreateAccount.setOnClickListener(v -> {
            String username = userName.getText().toString().trim();
            String emailInput = email.getText().toString().trim();
            String confirmEmail = cfEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String phone = phoneNumber.getText().toString().trim();

            if (username.isEmpty() || emailInput.isEmpty() || confirmEmail.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!emailInput.equals(confirmEmail)) {
                Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.userExists(emailInput)) {
                Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = db.addUser(username, emailInput, password, phone);
            if (success) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this, InterestActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
