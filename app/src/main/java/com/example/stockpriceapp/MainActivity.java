package com.example.stockpriceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<Quote> mDataset;
    private MyAdapter myAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final int SEARCHER_ACTIVITY_REQUEST_CODE = 1;
    private DecimalFormat df2 = new DecimalFormat("#.##");
    private final String sharedPreference = "shared preferences";
    private final String jsonKey = "task tool";
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);

        //If already saved, load data. If not, create new arrayList
        loadData();

        recyclerView = findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        myAdapter = new MyAdapter(mDataset);
        recyclerView.setAdapter(myAdapter);

        for (int i = 0; i < mDataset.size(); i++) {
            update(mDataset.get(i).getSymbol(), mDataset.get(i).getPurchasePrice(), i);
        }

        findViewById(R.id.add).setOnClickListener(v -> {
            //Go to searcher class and run onActivity result
            Intent intent = new Intent(MainActivity.this, Searcher.class);
            startActivityForResult(intent, SEARCHER_ACTIVITY_REQUEST_CODE);
        });

        findViewById(R.id.totalReturn).setOnClickListener(v -> {
            // getPercentChange gives ##.##% format
            double totalReturn = 0;
            for (int i = 0; i < mDataset.size(); i++) {
                totalReturn += mDataset.get(i).getPercentChange() * (mDataset.get(i).getTotalInvestment() / getTotalInvestment());
            }
            String stringReturn = Double.valueOf(df2.format(totalReturn)).toString() + "%";
            Toast.makeText(this, stringReturn, Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.save).setOnClickListener(v -> {
            saveData();
            Toast.makeText(this, "Well saved!", Toast.LENGTH_SHORT).show();
        });

        TextView portfolio = findViewById(R.id.myPortfolio);
        TextPaint paint = portfolio.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, portfolio.getTextSize(),
                new int[]{
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FDFAFA"),
                        Color.parseColor("#B7B5B5"),
                        Color.parseColor("#797777"),
                }, null, Shader.TileMode.CLAMP);
        portfolio.getPaint().setShader(textShader);

    }
    @Override
    // called when Searcher activity is finished!
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCHER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Quote myData = new Quote(data.getStringExtra("symbol"), data.getDoubleExtra("price", 0),
                        data.getDoubleExtra("change", 0), data.getDoubleExtra("percent", 0),
                        data.getDoubleExtra("purchasePrice", 0), data.getIntExtra("volume", 0));
                for (int i = 0; i < mDataset.size(); i++) {
                    if (mDataset.get(i).getSymbol().equals(myData.getSymbol())) {
                        Toast.makeText(this, "You already have this one", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mDataset.add(myData);
                //Automatically save the data using shared preferences
                myAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Insert Valid Input!", Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult

    //Calculates Total investment of portfolio
    private double getTotalInvestment() {
        double totalInvestment = 0;
        for (int i = 0; i < mDataset.size(); i++) {
            totalInvestment += mDataset.get(i).getTotalInvestment();
        }
        return totalInvestment;
    }

    //Save the arrayList
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(new GsonDeserializeExclusion())
                .create();
        String json = gson.toJson(mDataset);
        editor.putString(jsonKey, json);
        editor.apply();
    }

    //Load data when first start the app
    private void loadData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(sharedPreference, MODE_PRIVATE);
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new GsonDeserializeExclusion())
                    .create();
            String json = sharedPreferences.getString(jsonKey, null);
            Type type = new TypeToken<ArrayList<Quote>>() {}.getType();
            mDataset = gson.fromJson(json, type);
            if (mDataset == null) {
                mDataset = new ArrayList<>();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public class GsonDeserializeExclusion implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredClass() == SimpleDateFormat.class;
        }

        @Override
        public boolean shouldSkipClass(Class< ? > clazz) {
            return false;
        }
    }

    //Alert to save if back pressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wanna save your data before leaving?")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData();
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void update(String symbol, double priceInput, int position) {
        Intent returnIntent = new Intent();
        String lowerCaseSymbol = symbol.toLowerCase();
        String url = "https://cloud.iexapis.com/stable/stock/" + lowerCaseSymbol + "/quote/?token=sk_7cc85afbacd6429db92aa9c26760cca4";
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

                            Quote newData = new Quote(symbol, currentPrice, change, changePercent, priceInput, mDataset.get(position).getVolume());
                            mDataset.set(position, newData);

                            myAdapter.notifyItemChanged(position);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}