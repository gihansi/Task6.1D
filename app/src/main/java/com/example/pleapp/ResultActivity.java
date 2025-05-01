package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView q1Result, q2Result, q3Result;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        q1Result = findViewById(R.id.q1Result);
        q2Result = findViewById(R.id.q2Result);
        q3Result = findViewById(R.id.q3Result);
        continueButton = findViewById(R.id.continueButton);

        String q1 = getIntent().getStringExtra("q1_answer");
        String q2 = getIntent().getStringExtra("q2_answer");
        String q3 = getIntent().getStringExtra("q3_answer");

        String q1Feedback = getIntent().getStringExtra("q1_feedback");
        String q2Feedback = getIntent().getStringExtra("q2_feedback");
        String q3Feedback = getIntent().getStringExtra("q3_feedback");

        q1Result.setText("1. Question 1\nAnswer: " + q1 + "\n" + q1Feedback);
        q2Result.setText("2. Question 2\nAnswer: " + q2 + "\n" + q2Feedback);
        q3Result.setText("3. Question 3\nAnswer: " + q3 + "\n" + q3Feedback);

        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
