package com.example.stockpriceapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndividualStock extends AppCompatActivity {

    private static final String TAG = "IndividualStock";

    private LineChart mChart;

    private RequestQueue mQueue;
    //set all textView
    TextView date;
    TextView name;
    TextView price;
    TextView changeInPrice;
    TextView mrkCap;
    TextView beta;
    TextView pe;
    TextView volume;
    TextView purchasePrice;
    TextView myVolume;
    TextView proportion;
    TextView totalReturn;
    String change;

    //For graph
    ArrayList<Entry> yValues;
    Button Y5;
    Button Y1;
    Button M6;
    Button M3;
    Button M1;
    Button D5;
    Button D1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        mQueue = Volley.newRequestQueue(this);
        mChart = findViewById(R.id. lineChart);

        //Get symbol from mainActivity
        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");

        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        changeInPrice = findViewById(R.id.changeInPrice);
        mrkCap = findViewById(R.id.MrkCap_value);
        beta = findViewById(R.id.Beta_value);
        pe = findViewById(R.id.PE_value);
        volume = findViewById(R.id.Volume_value);
        purchasePrice = findViewById(R.id.PurchasePrice_value);
        myVolume = findViewById(R.id.MyVolume_value);
        proportion = findViewById(R.id.Proportion_value);
        totalReturn = findViewById(R.id.Return_value);

        //Call jsonParse to set all textView
        if (symbol == null) {
            finish();
        } else {
            jsonParse(symbol);
        }

        // enable some of features
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        yValues = new ArrayList<>();


        //call to fill for default (1d)
        jsonFill(symbol, "1d");

        //click 5Y
        Y5 = findViewById(R.id.Y5);
        Y5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "5y");
            }
        });

        //click 1Y
        Y1 = findViewById(R.id.Y1);
        Y1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "1y");
            }
        });

        //click 6M
        M6 = findViewById(R.id.M6);
        M6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "6m");
            }
        });

        //click 3M
        M3 = findViewById(R.id.M3);
        M3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "3m");
            }
        });

        //click 1M
        M1 = findViewById(R.id.M1);
        M1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "1m");
            }
        });

        //click 5D
        D5 = findViewById(R.id.D5);
        D5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "5d");
            }
        });

        //click 1D
        D1 = findViewById(R.id.D1);
        D1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yValues.clear();
                jsonFill(symbol, "1d");
            }
        });


    }

    // reference this youtube video: https://www.youtube.com/watch?v=y2xtLqP8dSQ
    private void jsonParse(String symbol) {
        String lowerCaseSymbol = symbol.toLowerCase();
        String url = "https://cloud.iexapis.com/stable/stock/" + lowerCaseSymbol + "/quote/?token=sk_7cc85afbacd6429db92aa9c26760cca4";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // add the symbol to the url and search if the symbol is valid
                        // if valid, add it and return to main activity
                        try {
                            if (response.has("latestTime") && !response.isNull("latestTime")) {
                                date.setText(response.getString("latestTime"));
                            } else {
                                date.setText("N/A");
                            }
                            if (response.has("companyName") && !response.isNull("companyName")) {
                                name.setText(response.getString("companyName"));
                            } else {
                                name.setText("N/A");
                            }
                            if (response.has("latestPrice") && !response.isNull("latestPrice")) {
                                price.setText(response.getString("latestPrice"));
                            } else {
                                price.setText("N/A");
                            }
                            if (response.has("marketCap") && !response.isNull("marketCap")) {
                                mrkCap.setText(response.getString("marketCap"));
                            } else {
                                mrkCap.setText("N/A");
                            }
                            //beta.setText(json.getString("companyName"));
                            if (response.has("peRatio") && !response.isNull("peRatio")) {
                                pe.setText(response.getString("peRatio"));
                            } else {
                                pe.setText("N/A");
                            }
                            if (response.has("avgTotalVolume") && !response.isNull("avgTotalVolume")) {
                                volume.setText(response.getString("avgTotalVolume"));
                            } else {
                                volume.setText("N/A");
                            }
                            purchasePrice.setText(getIntent().getStringExtra("purchasePrice"));
                            myVolume.setText(getIntent().getStringExtra("volume"));
                            proportion.setText(getIntent().getStringExtra("proportion"));
                            totalReturn.setText(getIntent().getStringExtra("return"));


                            if (getIntent().getBooleanExtra("isChangePositive", true)) {
                                change = "+" + getIntent().getStringExtra("change");
                                changeInPrice.setText(change);
                                changeInPrice.setTextColor(Color.GREEN);
                            } else {
                                change = "-" + getIntent().getStringExtra("change");
                                changeInPrice.setText(change);
                                changeInPrice.setTextColor(Color.RED);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                finish();
            }
        });
        mQueue.add(request);
    }

    //Fill out data from Json

    private void jsonFill(String symbol, String date) {
        String url = "https://cloud.iexapis.com/stable/stock/" + symbol.toLowerCase() + "/chart/" + date + "/?token=sk_7cc85afbacd6429db92aa9c26760cca4";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject unit;
                float price;
                try {
                    if (date.equals("1d")) {
                        for (int i = 0; i < response.length(); i += 10) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    } else if (date.equals("5d")) {
                        for (int i = 0; i < response.length(); i++) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    } else if (date.equals("1m")) {
                        for (int i = 0; i < response.length(); i++) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    } else if (date.equals("3m")) {
                        for (int i = 0; i < response.length(); i += 3) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    } else if (date.equals("6m")) {
                        for (int i = 0; i < response.length(); i += 7) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    } else if (date.equals("1y")) {
                        for (int i = 0; i < response.length(); i += 30) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    } else if (date.equals("5y")) {
                        for (int i = 0; i < response.length(); i += 100) {
                            unit = response.getJSONObject(i);
                            if (unit.has("close") && !unit.isNull("close")) {
                                price = Float.valueOf(unit.getString("close"));
                                yValues.add(new Entry(i, price));
                            }
                        }
                    }

                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                    set1.setFillAlpha(110);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    LineData data = new LineData(dataSets);

                    mChart.setData(data);

                    mChart.animateX(1500);

                    mChart.invalidate();


                } catch (JSONException e) {
                    e.printStackTrace();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                finish();
            }
        });
        mQueue.add(request);
    }

}