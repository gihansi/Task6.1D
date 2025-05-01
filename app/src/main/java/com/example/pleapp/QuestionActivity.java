package com.example.pleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity {

    LinearLayout layoutQ1, layoutQ2, layoutQ3;
    RadioGroup q1Group, q2Group, q3Group;
    Button nextToQ2, nextToQ3, submitBtn, backToQ1, backToQ2;

    String q1Answer, q2Answer, q3Answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        layoutQ1 = findViewById(R.id.layoutQ1);
        layoutQ2 = findViewById(R.id.layoutQ2);
        layoutQ3 = findViewById(R.id.layoutQ3);

        //q1
        q1Group = findViewById(R.id.question1Group);
        nextToQ2 = findViewById(R.id.btnNextQ2);

        //q2
        q2Group = findViewById(R.id.question2Group);
        nextToQ3 = findViewById(R.id.btnNextQ3);
        backToQ1 = findViewById(R.id.btnBackQ1);

        //q3
        q3Group = findViewById(R.id.question3Group);
        submitBtn = findViewById(R.id.btnSubmit);
        backToQ2 = findViewById(R.id.btnBackQ2);

        layoutQ1.setVisibility(View.VISIBLE);
        layoutQ2.setVisibility(View.GONE);
        layoutQ3.setVisibility(View.GONE);

        //q1 → q2
        nextToQ2.setOnClickListener(v -> {
            int selected = q1Group.getCheckedRadioButtonId();
            if (selected == -1) {
                Toast.makeText(this, "Select an answer for Question 1", Toast.LENGTH_SHORT).show();
                return;
            }
            q1Answer = ((RadioButton) findViewById(selected)).getText().toString();
            layoutQ1.setVisibility(View.GONE);
            layoutQ2.setVisibility(View.VISIBLE);
        });

        //q2 → q3
        nextToQ3.setOnClickListener(v -> {
            int selected = q2Group.getCheckedRadioButtonId();
            if (selected == -1) {
                Toast.makeText(this, "Select an answer for Question 2", Toast.LENGTH_SHORT).show();
                return;
            }
            q2Answer = ((RadioButton) findViewById(selected)).getText().toString();

            layoutQ2.setVisibility(View.GONE);
            layoutQ3.setVisibility(View.VISIBLE);

            submitBtn.setVisibility(View.VISIBLE);
        });


        //q3 → Submit
        submitBtn.setOnClickListener(v -> {
            int selected = q3Group.getCheckedRadioButtonId();
            if (selected == -1) {
                Toast.makeText(this, "Select an answer for Question 3", Toast.LENGTH_SHORT).show();
                return;
            }
            q3Answer = ((RadioButton) findViewById(selected)).getText().toString();

            String q1Correct = "Answer 2";
            String q2Correct = "True";
            String q3Correct = "42";

            String q1Feedback = q1Answer.equals(q1Correct) ? "✅ Correct" : "❌ Wrong";
            String q2Feedback = q2Answer.equals(q2Correct) ? "✅ Correct" : "❌ Wrong";
            String q3Feedback = q3Answer.equals(q3Correct) ? "✅ Correct" : "❌ Wrong";

            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            intent.putExtra("q1_answer", q1Answer);
            intent.putExtra("q2_answer", q2Answer);
            intent.putExtra("q3_answer", q3Answer);

            intent.putExtra("q1_feedback", q1Feedback);
            intent.putExtra("q2_feedback", q2Feedback);
            intent.putExtra("q3_feedback", q3Feedback);

            startActivity(intent);
        });

        //q2 → q1
        backToQ1.setOnClickListener(v -> {
            layoutQ2.setVisibility(View.GONE);
            layoutQ1.setVisibility(View.VISIBLE);
        });

        //q3 → q2
        backToQ2.setOnClickListener(v -> {
            layoutQ3.setVisibility(View.GONE);
            layoutQ2.setVisibility(View.VISIBLE);
        });
    }
}
