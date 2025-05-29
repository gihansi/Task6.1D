package com.example.pleapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class InterestActivity extends AppCompatActivity {

    Button nextButton;
    HashMap<Button, String> topicButtons = new HashMap<>();
    ArrayList<String> selectedTopics = new ArrayList<>();
    int MAX_SELECTION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        nextButton = findViewById(R.id.nextButton);

        String[] topics = {"Algorithms", "Data Structures", "Web Development", "Testing",
                "AI", "Machine Learning", "Databases", "Networking", "Cloud", "Cybersecurity"};

        for (String topic : topics) {
            // Use findViewByText by looping all views OR manually map like below:
            Button btn = findButtonByText(topic);
            if (btn != null) {
                topicButtons.put(btn, topic);
                btn.setOnClickListener(v -> toggleSelection(btn));
            }
        }

        nextButton.setOnClickListener(v -> {
            if (selectedTopics.size() == 0) {
                Toast.makeText(this, "Please select at least one topic", Toast.LENGTH_SHORT).show();
            } else {
                // You can save selectedTopics or pass via intent
                Toast.makeText(this, "Selected: " + selectedTopics, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InterestActivity.this, MainActivity.class));
            }
        });
    }

    private void toggleSelection(Button button) {
        String topic = topicButtons.get(button);
        if (selectedTopics.contains(topic)) {
            selectedTopics.remove(topic);
            button.setBackgroundColor(Color.parseColor("#E0E0E0")); // unselected color
        } else {
            if (selectedTopics.size() >= MAX_SELECTION) {
                Toast.makeText(this, "You can only select up to 10 topics", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedTopics.add(topic);
            button.setBackgroundColor(Color.parseColor("#009688")); // selected color
        }
    }

    private Button findButtonByText(String text) {
        for (Button btn : getAllButtons()) {
            if (btn.getText().toString().equalsIgnoreCase(text)) {
                return btn;
            }
        }
        return null;
    }

    private ArrayList<Button> getAllButtons() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.nextButton));
        View root = findViewById(android.R.id.content);
        addButtonsRecursively(root, buttons);
        buttons.remove(findViewById(R.id.nextButton));
        return buttons;
    }

    private void addButtonsRecursively(View view, ArrayList<Button> list) {
        if (view instanceof Button) {
            list.add((Button) view);
        } else if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                addButtonsRecursively(group.getChildAt(i), list);
            }
        }
    }
}
