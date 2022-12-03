package com.project.i190426_i190435_i190660;

import java.io.Serializable;

public class OrderItem implements Serializable {
    Product p;
    int quantity;
    double price;

    public OrderItem(Product p, int quantity, double price) {
        this.p = p;
        this.quantity = quantity;
        this.price=price;
    }

    public Product getP() {
        return p;
    }

    public void setP(Product p) {
        this.p = p;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
