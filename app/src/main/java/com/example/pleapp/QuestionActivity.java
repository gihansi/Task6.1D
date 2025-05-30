package com.example.pleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    private LinearLayout questionsContainer;
    private LinearLayout loadingContainer;
    private Button submitButton;

    private ArrayList<String> questionList = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsList = new ArrayList<>();
    private ArrayList<String> correctAnswers = new ArrayList<>();
    private final ArrayList<RadioGroup> radioGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionsContainer = findViewById(R.id.questionsContainer);
        loadingContainer = findViewById(R.id.loadingContainer);
        submitButton = findViewById(R.id.submitButton);

        String url = "http://10.0.2.2:5000/getQuiz?topic=movies";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingContainer.setVisibility(View.GONE);
                        try {
                            Object quizObj = response.get("quiz");

                            if (quizObj instanceof JSONArray) {
                                JSONArray quizArray = (JSONArray) quizObj;

                                for (int i = 0; i < quizArray.length(); i++) {
                                    JSONObject questionObj = quizArray.getJSONObject(i);
                                    String question = questionObj.getString("question");
                                    JSONArray options = questionObj.getJSONArray("options");
                                    String correctAnswerLetter = questionObj.getString("correct_answer");

                                    questionList.add(question);

                                    ArrayList<String> opts = new ArrayList<>();
                                    for (int j = 0; j < options.length(); j++) {
                                        opts.add(options.getString(j));
                                    }
                                    optionsList.add(opts);
                                    String correctAnswerText = "";
                                    switch (correctAnswerLetter.toUpperCase()) {
                                        case "A":
                                            correctAnswerText = opts.get(0);
                                            break;
                                        case "B":
                                            correctAnswerText = opts.get(1);
                                            break;
                                        case "C":
                                            correctAnswerText = opts.get(2);
                                            break;
                                        case "D":
                                            correctAnswerText = opts.get(3);
                                            break;
                                    }
                                    correctAnswers.add(correctAnswerText);
                                }

                                displayQuestions();

                            } else if (quizObj instanceof String) {
                                Toast.makeText(getApplicationContext(), "Test Response: " + quizObj, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Unexpected format in 'quiz'", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingContainer.setVisibility(View.GONE);

                        String errorMsg = "Unknown error";
                        if (error.networkResponse != null) {
                            errorMsg = "HTTP " + error.networkResponse.statusCode + ": " + new String(error.networkResponse.data);
                        } else if (error.getMessage() != null) {
                            errorMsg = error.getMessage();
                        }
                        Log.e(TAG, "Error fetching quiz: " + errorMsg, error);
                        Toast.makeText(QuestionActivity.this, "Error fetching quiz: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        loadingContainer.setVisibility(View.VISIBLE);
        queue.add(jsonObjectRequest);

        submitButton.setOnClickListener(v -> onSubmit());
    }

    private void displayQuestions() {
        questionsContainer.removeAllViews();

        for (int i = 0; i < questionList.size(); i++) {
            String question = questionList.get(i);
            ArrayList<String> options = optionsList.get(i);

            TextView questionText = new TextView(this);
            questionText.setText((i + 1) + ". " + question);
            questionText.setTextSize(18f);
            questionText.setPadding(0, 20, 0, 10);
            questionsContainer.addView(questionText);

            RadioGroup radioGroup = new RadioGroup(this);
            radioGroups.add(radioGroup);

            for (String option : options) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioGroup.addView(radioButton);
            }
            questionsContainer.addView(radioGroup);
        }

        submitButton.setVisibility(View.VISIBLE);
    }

    private void onSubmit() {
        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);

        int totalQuestions = questionList.size();

        for (int i = 0; i < totalQuestions; i++) {
            RadioGroup radioGroup = radioGroups.get(i);

            int selectedId = radioGroup.getCheckedRadioButtonId();

            String userAnswer = "No answer selected";
            if (selectedId != -1) {
                RadioButton selectedRadio = radioGroup.findViewById(selectedId);
                if (selectedRadio != null) {
                    userAnswer = selectedRadio.getText().toString();
                }
            }

            String feedback = userAnswer.trim().equalsIgnoreCase(correctAnswers.get(i).trim()) ?
                    "Correct!" :
                    "Incorrect! Correct answer: " + correctAnswers.get(i);

            intent.putExtra("q" + (i + 1) + "_answer", userAnswer);
            intent.putExtra("q" + (i + 1) + "_feedback", feedback);
        }
        saveAttemptToHistory();

        startActivity(intent);
    }

    private void saveAttemptToHistory() {
        JSONArray attemptArray = new JSONArray();
        long timestamp = System.currentTimeMillis();

        for (int i = 0; i < questionList.size(); i++) {
            JSONObject q = new JSONObject();
            try {
                q.put("question", questionList.get(i));
                q.put("correct", correctAnswers.get(i));

                RadioGroup radioGroup = (RadioGroup) questionsContainer.getChildAt(i * 2 + 1);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String userAnswer = "No answer selected";

                if (selectedId != -1) {
                    RadioButton selectedRadio = radioGroup.findViewById(selectedId);
                    if (selectedRadio != null) {
                        userAnswer = selectedRadio.getText().toString();
                    }
                }

                q.put("userAnswer", userAnswer);
                q.put("isCorrect", userAnswer.trim().equalsIgnoreCase(correctAnswers.get(i).trim()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            attemptArray.put(q);
        }

        JSONObject historyObject = new JSONObject();
        try {
            historyObject.put("timestamp", timestamp);
            historyObject.put("attempt", attemptArray);

            SharedPreferences prefs = getSharedPreferences("quiz_history", MODE_PRIVATE);
            String oldData = prefs.getString("history", "[]");
            JSONArray history = new JSONArray(oldData);
            history.put(historyObject);

            prefs.edit().putString("history", history.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
