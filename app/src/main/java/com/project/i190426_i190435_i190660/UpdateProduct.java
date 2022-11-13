package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateProduct extends AppCompatActivity {

    Product product;
    EditText name, quantity, price, description, category;
    ImageView image, back;
    Button update;
    Uri selectedImage= null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        name=findViewById(R.id.name);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);
        category=findViewById(R.id.category);
        image=findViewById(R.id.image);
        back=findViewById(R.id.back);
        update=findViewById(R.id.update);



        product=new Product(
                getIntent().getIntExtra("id", 0),
                getIntent().getStringExtra("name"),
                getIntent().getDoubleExtra("price", 0),
                getIntent().getIntExtra("quantity", 0),
                getIntent().getStringExtra("description"),
                getIntent().getStringExtra("image"),
                getIntent().getStringExtra("category"));

        name.setText(product.getName());
        quantity.setText(String.valueOf(product.getQuantity()));
        price.setText(String.valueOf(product.getPrice()));
        description.setText(product.getDescription());
        category.setText(product.getCategory());
        int id=getResources().getIdentifier(product.getImage(), "drawable", getPackageName());
        image.setImageResource(id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(i, "Choose your Image"),
                        200
                );

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            image.setImageURI(selectedImage);
        }
    }
}