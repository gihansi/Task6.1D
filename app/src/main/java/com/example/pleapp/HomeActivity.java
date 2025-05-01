package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button startTaskButton;
    LinearLayout taskCard;
    ImageButton goToTaskBtn;  // NEW

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        taskCard = findViewById(R.id.taskCard);
        goToTaskBtn = findViewById(R.id.goToTaskBtn);
       if (startTaskButton != null) {
            startTaskButton.setOnClickListener(v -> {
                Toast.makeText(this, "Starting task...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, QuestionActivity.class));
            });
        }
        taskCard.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Task...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, QuestionActivity.class));
        });

        goToTaskBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Launching task...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, QuestionActivity.class));
        });
    }
}
