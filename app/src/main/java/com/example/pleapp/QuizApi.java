package com.example.pleapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class QuizApi {

    public interface QuizCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void fetchQuiz(Context context, String topic, QuizCallback callback) {
        String url = "http://127.0.0.1:5000/getQuiz?topic=" + topic; // Replace with your backend URL

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> callback.onSuccess(response),
                error -> callback.onError(error.toString())
        );

        queue.add(stringRequest);
    }
}
