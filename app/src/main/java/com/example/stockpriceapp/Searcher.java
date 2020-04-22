package com.example.stockpriceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Searcher extends AppCompatActivity {
    Button addStock;
    private RequestQueue mQueue;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcher);

        mQueue = Volley.newRequestQueue(this);
        input = findViewById(R.id.input);
        addStock = findViewById(R.id.addStock);
        addStock.setOnClickListener(v -> {
            // reference https://www.codercrunch.com/post/1763802291/build-a-stock-market-app-in-android
            String inputSymbol = input.getText().toString();
            if (inputSymbol.equalsIgnoreCase("")) {
                Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_LONG).show();
            }
            else {
                jsonParse(inputSymbol);
                //Back to Main Activity
                finish();
            }

        });

    }
    // reference this youtube video: https://www.youtube.com/watch?v=y2xtLqP8dSQ
    private void jsonParse(String symbol) {
        String url = "https://cloud.iexapis.com/stable/stock/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

}
