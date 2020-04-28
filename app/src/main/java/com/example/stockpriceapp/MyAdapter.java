package com.example.stockpriceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Quote> mDataset;
    MyAdapter(List<Quote> myDataset) {
        mDataset = myDataset;
    }
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    boolean isChangePositive;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.symbol.setText(mDataset.get(position).getSymbol());
        holder.currentPrice.setText(String.valueOf(mDataset.get(position).getPrice()));
        holder.change.setText(String.valueOf(mDataset.get(position).getChangeInPrice()));
        holder.changePercent.setText(String.valueOf(mDataset.get(position).getPercentChange()));
        String totalInvestment = Double.valueOf(df2.format(mDataset.get(position).getTotalInvestment())).toString();
        holder.total.setText(totalInvestment);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, IndividualStock.class);

                //format all the values
                if (mDataset.get(position).getChangeInPrice() > 0) {
                    isChangePositive = true;
                }  else {
                    isChangePositive = false;
                }
                String change = Double.valueOf(Math.abs(mDataset.get(position).getChangeInPrice())).toString();
                String price = Double.valueOf(df2.format(mDataset.get(position).getPurchasePrice())).toString();
                String volume = Double.valueOf(df2.format(mDataset.get(position).getVolume())).toString();
                String proportion = Double.valueOf(df2.format(mDataset.get(position).getTotalInvestment() / totalInvestment() * 100)).toString() + "%";
                String stringReturn = Double.valueOf(df2.format(mDataset.get(position).getPercentChange())).toString() + "%";


                intent.putExtra("symbol", mDataset.get(position).getSymbol());
                intent.putExtra("change", change);
                intent.putExtra("purchasePrice", price);
                intent.putExtra("volume", volume);
                intent.putExtra("isChangePositive", isChangePositive);
                intent.putExtra("proportion", proportion);
                intent.putExtra("return", stringReturn);
                //Start the individual activity
                context.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // if long click, remove
    private void remove(int position) {
        try {
            mDataset.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView symbol;
        TextView currentPrice;
        TextView change;
        TextView changePercent;
        TextView total;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.symbol = (TextView) itemView.findViewById(R.id.symbol);
            this.currentPrice = (TextView) itemView.findViewById(R.id.currentPrice);
            this.change = (TextView) itemView.findViewById(R.id.change);
            this.changePercent = (TextView) itemView.findViewById(R.id.changePercent);
            this.total = (TextView) itemView.findViewById(R.id.total);

        }
    }

    private double totalInvestment() {
        double totalInvestment = 0;
        for (int i = 0; i < mDataset.size(); i++) {
            totalInvestment += mDataset.get(i).getTotalInvestment();
        }
        return totalInvestment;
    }

}
