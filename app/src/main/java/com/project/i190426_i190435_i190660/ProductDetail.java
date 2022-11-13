package com.project.i190426_i190435_i190660;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetail extends AppCompatActivity {

    Product product;
    TextView name, quantity, price, description;
    ImageView image, back;
    ImageButton plus, minus;
    Button addTocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        name=findViewById(R.id.name);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);
        image=findViewById(R.id.image);
        back=findViewById(R.id.back);
        addTocart=findViewById(R.id.addTocart);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);

        product=new Product(
                getIntent().getIntExtra("id", 0),
                getIntent().getStringExtra("name"),
                getIntent().getDoubleExtra("price", 0),
                getIntent().getStringExtra("description"),
                getIntent().getStringExtra("image"),
                getIntent().getStringExtra("category"));

        name.setText(product.getName());
        price.setText("Rs. "+String.valueOf(product.getPrice()));
        description.setText(product.getDescription());
        int id=getResources().getIdentifier(product.getImage(), "drawable", getPackageName());
        image.setImageResource(id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(quantity.getText().toString());
                q=q+1;
                quantity.setText(String.valueOf(q));

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(quantity.getText().toString());
                if(q>1){
                    q=q-1;
                    quantity.setText(String.valueOf(q));
                }

            }
        });


    }
}