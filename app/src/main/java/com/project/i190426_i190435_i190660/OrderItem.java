package com.project.i190426_i190435_i190660;

import java.io.Serializable;

public class OrderItem implements Serializable {
    Product p;
    int quantity;

    public OrderItem(Product p, int quantity) {
        this.p = p;
        this.quantity = quantity;
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
}
