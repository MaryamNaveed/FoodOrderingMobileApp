package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainPageRestaurant extends AppCompatActivity {

    List<Product> allProducts;
    MyAdapterRestaurantProducts adapter;

    ImageView add, menu;
    TextView addProduct, logout;
    DrawerLayout drawer;
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_restaurant);

        allProducts=new ArrayList<>();

        menu=findViewById(R.id.menu);
        add=findViewById(R.id.add);
        addProduct=findViewById(R.id.addProduct);
        logout=findViewById(R.id.logout);
        drawer=findViewById(R.id.drawer);
        rv=findViewById(R.id.rv);

        allProducts.add(new Product(1, "Hamburger", 700, 20,
                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
                "burger", "Burger"));
        allProducts.add(new Product(2, "Vegetable Pizza", 1200, 20,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza"));
        allProducts.add(new Product(3, "Chicken Biryani", 900, 20,
                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
                "Rice"));

        allProducts.add(new Product(4, "Hamburger", 700, 20,
                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
                "burger", "Burger"));
        allProducts.add(new Product(5, "Vegetable Pizza", 1200, 20,
                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
                "pizza", "Pizza"));
        allProducts.add(new Product(6, "Chicken Biryani", 900, 20,
                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
                "Rice"));

        adapter=new MyAdapterRestaurantProducts(allProducts, MainPageRestaurant.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPageRestaurant.this);
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageRestaurant.this, AddProduct.class);
                startActivity(intent);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageRestaurant.this, AddProduct.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageRestaurant.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}