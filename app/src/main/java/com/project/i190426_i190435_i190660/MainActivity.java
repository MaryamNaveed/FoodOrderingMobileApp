package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button restaurant, customer;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurant=findViewById(R.id.restaurant);
        customer=findViewById(R.id.customer);

        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();

        if(mPref.getBoolean("loggedInAdmin", false)){
            Intent intent=new Intent(MainActivity.this, MainPageRestaurant.class);
            startActivity(intent);

            finish();

        }
        else if(mPref.getBoolean("loggedInCustomer", false)){
            Intent intent=new Intent(MainActivity.this, MainPageCustomer.class);
            startActivity(intent);

            finish();
        }

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, LoginAdmin.class);
                startActivity(intent);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, LoginCustomer.class);
                startActivity(intent);
            }
        });
    }
}