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


import org.json.JSONException;
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
            }

        });

    }
    // reference this youtube video: https://www.youtube.com/watch?v=y2xtLqP8dSQ
    private void jsonParse(String symbol) {
        Intent returnIntent = new Intent();
        String lowerCaseSymbol = symbol.toLowerCase();
        String url = "https://cloud.iexapis.com/stable/stock/" + lowerCaseSymbol + "/quote/?token=pk_08fb66c38ef941ac98cbeb5cf45cf0a2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // add the symbol to the url and search if the symbol is valid
                        // if valid, add it and return to main activity
                        try {
                            returnIntent.putExtra("symbol", symbol);
                            returnIntent.putExtra("price", response.getDouble("latestPrice"));
                            returnIntent.putExtra("change", response.getDouble("change"));
                            returnIntent.putExtra("percent", response.getDouble("changePercent") * 100);
                            setResult(RESULT_OK, returnIntent);
                            finish();

                        } catch (JSONException e) {
                            setResult(RESULT_OK, returnIntent);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setResult(RESULT_OK, returnIntent);
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

}
