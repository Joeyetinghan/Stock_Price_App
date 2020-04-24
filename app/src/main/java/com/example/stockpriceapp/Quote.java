package com.example.stockpriceapp;

import java.text.NumberFormat;
// Reference https://github.com/IntertechInc/android-recycler/blob/master/app/src/main/java/lollipop/intertech/com/recycler/domain/Quote.java
public class Quote {
    private String symbol;
    private double price;
    private double changeInPrice;
    private double percentChange;
    private NumberFormat dollarFormat = NumberFormat.getCurrencyInstance();
    private int numberOfShares;
    private double amountPaid;
    public Quote(String setSymbol, double setPrice, double setChangeInPrice, double setPercentChange) {
        this.symbol = setSymbol;
        price = setPrice;
        changeInPrice = setChangeInPrice;
        percentChange = setPercentChange;
        //numberOfShares = setNumberOfShares;
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
    public String getFormattedPrice() {
        return dollarFormat.format(getPrice());
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

}
