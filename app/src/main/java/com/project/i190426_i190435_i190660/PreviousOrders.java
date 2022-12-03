package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrders extends AppCompatActivity {

    ImageView back;
    RecyclerView rv;
    List<Order> orders;
    MyAdapterOrder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);
        back=findViewById(R.id.back);
        rv=findViewById(R.id.rv);

        List<OrderItem> cartItems1=new ArrayList<>();

        Product p1=new Product(1, "Hamburger", 700,
                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
                "burger", "Burger");

        cartItems1.add(new OrderItem(p1, 5, p1.price));

        Product p2=new Product(5, "Vegetable Pizza", 1200,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza");

        cartItems1.add(new OrderItem(p2, 2, p2.price));

        List<OrderItem> cartItems2=new ArrayList<>();

        Product p3=new Product(6, "Chicken Biryani", 900,
                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
                "Rice");

        cartItems2.add(new OrderItem(p3, 5, p3.price));

        Product p4=new Product(5, "Vegetable Pizza", 1200,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza");

        cartItems2.add(new OrderItem(p4, 2, p4.price));

        orders=new ArrayList<>();


        orders.add(new Order(cartItems1, "25 July 2022", 20));
        orders.add(new Order(cartItems2, "25 August 2022", 20));

        adapter=new MyAdapterOrder(orders, PreviousOrders.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(PreviousOrders.this);
        rv.setLayoutManager(lm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}