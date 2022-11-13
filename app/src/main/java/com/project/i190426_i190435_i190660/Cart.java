package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    List<CartItems> cartItems;
    MyAdapterCart adapter;
    ImageView back;
    RecyclerView rv;
    TextView itemTotal, total, tax;
    double taxCal=20, itemTotalCal=0, totalCal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back=findViewById(R.id.back);
        cartItems=new ArrayList<>();
        rv=findViewById(R.id.rv);
        itemTotal=findViewById(R.id.itemTotal);
        tax=findViewById(R.id.tax);
        total=findViewById(R.id.total);

        cartItems.add(new CartItems(new Product(1, "Hamburger", 700,
                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
                "burger", "Burger"), 5));

        cartItems.add(new CartItems(new Product(5, "Vegetable Pizza", 1200,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza"), 2));




        adapter=new MyAdapterCart(cartItems, Cart.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(Cart.this);
        rv.setLayoutManager(lm);

        calculatePrice();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void calculatePrice(){

        itemTotalCal=0;
        taxCal=20;

        for(int i=0; i<cartItems.size(); i++){
            itemTotalCal+=(cartItems.get(i).getQuantity()*cartItems.get(i).getP().getPrice());
        }

        itemTotal.setText(String.valueOf(itemTotalCal));

        tax.setText(String.valueOf(taxCal));

        total.setText(String.valueOf(taxCal+itemTotalCal));

    }
}