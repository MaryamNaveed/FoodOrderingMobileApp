package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class OrderedItems extends AppCompatActivity {

    List<Product> allProducts;
    MyAdapterCustomerProducts adapter;
    RecyclerView rv;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_items);


        allProducts=new ArrayList<>();
        back=findViewById(R.id.back);
        rv=findViewById(R.id.rv);

        Order o=(Order) getIntent().getSerializableExtra("order");



        for(int i=0; i<o.getOrderItemList().size(); i++){
            allProducts.add(o.getOrderItemList().get(i).getP());
        }


        adapter=new MyAdapterCustomerProducts(allProducts, OrderedItems.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(OrderedItems.this);
        rv.setLayoutManager(lm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}