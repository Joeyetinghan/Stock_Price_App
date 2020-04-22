package com.example.stockpriceapp;

import java.text.NumberFormat;
// Reference https://github.com/IntertechInc/android-recycler/blob/master/app/src/main/java/lollipop/intertech/com/recycler/domain/Quote.java
public class Quote {
    private double price;
    private double change_in_price;
    private double percentChange;
    private String symbol;
    private NumberFormat dollarFormat
            = NumberFormat.getCurrencyInstance();
    public Quote(String symbol) {
        this.symbol = symbol;
    }
    public double getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return dollarFormat.format(getPrice());
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getChange_in_price() {
        return change_in_price;
    }
}
