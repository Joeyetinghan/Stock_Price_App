package com.example.stockpriceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Quote> mDataset;
    MyAdapter(List<Quote> myDataset) {
        mDataset = myDataset;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.symbol.setText(mDataset.get(position).getSymbol());
        holder.currentPrice.setText(mDataset.get(position).getFormattedPrice());
        holder.change.setText(String.valueOf(mDataset.get(position).getChangeInPrice()));
        holder.changePercent.setText(String.valueOf(mDataset.get(position).getPercentChange()));
        holder.total.setText(mDataset.get(position).getTotal());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symbol = holder.symbol.getText().toString();
                Toast.makeText(v.getContext(), mDataset.get(position).getTotal(), Toast.LENGTH_SHORT).show();
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
}
