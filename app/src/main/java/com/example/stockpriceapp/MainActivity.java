package com.example.stockpriceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Quote> mDataset;
    private MyAdapter myAdapter;
    private LinearLayoutManager linearLayoutManager;
    private static final int SEARCHER_ACTIVITY_REQUEST_CODE = 1;
    double totalVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDataset = new ArrayList<>();
        myAdapter = new MyAdapter(mDataset);
        recyclerView.setAdapter(myAdapter);

        for (int i = 0; i < mDataset.size(); i++) {
            totalVolume += mDataset.get(i).getVolume();
        }

        findViewById(R.id.add).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Searcher.class);
            startActivityForResult(intent, SEARCHER_ACTIVITY_REQUEST_CODE);
        });

        findViewById(R.id.totalReturn).setOnClickListener(v -> {
            // getPercentChange gives ##.##% format
            double totalReturn = 0;
            for (int i = 0; i < mDataset.size(); i++) {
                totalReturn += mDataset.get(i).getPercentChange() * (mDataset.get(i).getVolume() / totalVolume);
            }
            String stringReturn = Double.toString(totalReturn);
            Toast.makeText(this, stringReturn, Toast.LENGTH_LONG).show();
        });

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
                        Toast.makeText(this, "You already have this one", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                mDataset.add(myData);
                myAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Insert Valid Input!", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult
}
