package com.project.i190426_i190435_i190660;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    List<OrderItem> orderItemList;
    String date;
    double tax;

    public Order(List<OrderItem> orderItemList, String date, double tax) {
        this.orderItemList = orderItemList;
        this.date=date;
        this.tax=tax;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
}
