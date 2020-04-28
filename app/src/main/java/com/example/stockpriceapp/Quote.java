package com.example.stockpriceapp;

import java.text.NumberFormat;
// Reference https://github.com/IntertechInc/android-recycler/blob/master/app/src/main/java/lollipop/intertech/com/recycler/domain/Quote.java
public class Quote {
    private String symbol;
    private double price;
    private double changeInPrice;
    private double percentChange;
    private double total;
    private int volume;
    private double purchasePrice;

    public Quote(String setSymbol, double setPrice, double setChangeInPrice, double setPercentChange, double setPurchasePrice, int setVolume) {
        this.symbol = setSymbol;
        price = setPrice;
        changeInPrice = setChangeInPrice;
        percentChange = setPercentChange;
        volume = setVolume;
        total = setPurchasePrice * setVolume;
        purchasePrice = setPurchasePrice;
    }
    public String getSymbol() {
    return symbol;
}
    public void setSymbol(String setSymbol) {
        symbol = setSymbol;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getChangeInPrice() {
        return changeInPrice;
    }
    public void setChangeInPrice(double setChangeInPrice) {
        changeInPrice = setChangeInPrice;
    }
    public double getPercentChange() {
        return percentChange;
    }
    public void setPercentChange(double setPercentChange) {
        percentChange = setPercentChange;
    }
    public double getTotalInvestment() {
        return total;
    }
    public int getVolume() {
        return volume;
    }
    public double getPurchasePrice() {
        return purchasePrice;
    }
}
