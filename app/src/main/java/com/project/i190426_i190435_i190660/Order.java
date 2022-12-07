package com.project.i190426_i190435_i190660;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    int id;
    List<OrderItem> orderItemList;
    String date;
    double tax;
    String status;

    public Order(int id, List<OrderItem> orderItemList, String date, double tax, String status) {
        this.id=id;
        this.orderItemList = orderItemList;
        this.date=date;
        this.tax=tax;
        this.status=status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
