package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainPageCustomer extends AppCompatActivity {

    List<Product> allProducts;
    MyAdapterCustomerProducts adapter;

    ImageView cart, menu;
    TextView logout, profile, prevOrders, cartItems;
    DrawerLayout drawer;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_customer);

        allProducts=new ArrayList<>();

        menu=findViewById(R.id.menu);
        cart=findViewById(R.id.cart);
        cartItems=findViewById(R.id.cartItems);
        profile=findViewById(R.id.profile);
        prevOrders=findViewById(R.id.prevOrders);
        logout=findViewById(R.id.logout);
        drawer=findViewById(R.id.drawer);
        rv=findViewById(R.id.rv);

        allProducts.add(new Product(1, "Hamburger", 700,
                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
                "burger", "Burger"));
        allProducts.add(new Product(2, "Vegetable Pizza", 1200,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza"));
        allProducts.add(new Product(3, "Chicken Biryani", 900,
                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
                "Rice"));

        allProducts.add(new Product(4, "Hamburger", 700,
                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
                "burger", "Burger"));
        allProducts.add(new Product(5, "Vegetable Pizza", 1200,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza"));
        allProducts.add(new Product(6, "Chicken Biryani", 900,
                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
                "Rice"));


        adapter=new MyAdapterCustomerProducts(allProducts, MainPageCustomer.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPageCustomer.this);
        rv.setLayoutManager(lm);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(Gravity.LEFT)){
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else{
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageCustomer.this, Cart.class);
                startActivity(intent);
            }
        });

        cartItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageCustomer.this, Cart.class);
                startActivity(intent);
            }
        });

        prevOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageCustomer.this, PreviousOrders.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageCustomer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageCustomer.this, Profile.class);
                startActivity(intent);
            }
        });
    }
}