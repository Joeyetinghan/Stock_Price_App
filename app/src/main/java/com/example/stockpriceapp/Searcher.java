package com.example.stockpriceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class Searcher extends AppCompatActivity {
    Button addStock;
    private RequestQueue mQueue;
    EditText input;
    EditText price;
    EditText volume;
    double priceInput;
    int volumeInput;
    String inputSymbol;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcher);

        mQueue = Volley.newRequestQueue(this);
        input = findViewById(R.id.stockInput);
        price = findViewById(R.id.priceInput);
        volume = findViewById(R.id.volumeInput);


        addStock = findViewById(R.id.addStock);
        addStock.setOnClickListener(v -> {
            // reference https://www.codercrunch.com/post/1763802291/build-a-stock-market-app-in-android
            try {
                inputSymbol = input.getText().toString();
                priceInput = Double.parseDouble(price.getText().toString());
                volumeInput = Integer.parseInt(volume.getText().toString());
                if (inputSymbol.equalsIgnoreCase("")) {
                    Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_LONG).show();
                }
                else {
                    jsonParse(inputSymbol, priceInput, volumeInput);
                }
            } catch (Exception e) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        LinearLayout add = findViewById(R.id.add);
    }
    // reference this youtube video: https://www.youtube.com/watch?v=y2xtLqP8dSQ
    private void jsonParse(String symbol, double priceInput, int volume) {
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
                            double currentPrice = response.getDouble("latestPrice");
                            double change = Double.valueOf(df2.format(currentPrice - priceInput));
                            double changePercent = Double.valueOf(df2.format(change / priceInput * 100));
                            returnIntent.putExtra("symbol", response.getString("symbol"));
                            returnIntent.putExtra("price", currentPrice);
                            returnIntent.putExtra("change", change);
                            returnIntent.putExtra("percent", changePercent);
                            returnIntent.putExtra("purchasePrice", priceInput);
                            returnIntent.putExtra("volume", volume);
                            setResult(RESULT_OK, returnIntent);
                            finish();

                        } catch (JSONException e) {
                            setResult(RESULT_CANCELED, returnIntent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        mQueue.add(request);
    }
}
