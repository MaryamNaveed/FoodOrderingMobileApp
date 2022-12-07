package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {

    Product product;
    TextView name, quantity, price, description;
    ImageView image, back;
    ImageButton plus, minus;
    Button addTocart;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

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

        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();

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
//        int id=getResources().getIdentifier(product.getPhoto(), "drawable", getPackageName());
//        image.setImageResource(id);

        Picasso.get().load(Uri.parse(Ip.ipAdd+"/"+product.getPhoto())).into(image);

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

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Ip.isConnected(getApplicationContext())){
                    addTocartfunc();
                }
                else{
                    Toast.makeText(ProductDetail.this, "You are offline", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    public void addTocartfunc(){
        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/addTocart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {

                                int idUser=res.getInt("id");

                                Toast.makeText(ProductDetail.this, "Added to cart", Toast.LENGTH_LONG).show();

                                addToSqliteCart();

                                Intent intent=new Intent(ProductDetail.this, Cart.class);
                                startActivity(intent);
                                finish();


                            } else {
                                Toast.makeText(ProductDetail.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDetail.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                            Toast.makeText(ProductDetail.this, response, Toast.LENGTH_LONG).show();
                            System.out.println(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetail.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(ProductDetail.this, error.toString(), Toast.LENGTH_LONG).show();

                    }


                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("item_id", String.valueOf(product.getId()));
                params.put("quantity", quantity.getText().toString());
                params.put("customer_id", String.valueOf(mPref.getInt("id", 0)));


                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(ProductDetail.this);
        queue.add(request);
    }

    public void addToSqliteCart(){


        MyDBHelper helper = new MyDBHelper(ProductDetail.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyProject.MyCart.CUST_ID, mPref.getInt("id", 0));
        cv.put(MyProject.MyCart.ITEM_ID, product.getId());
        cv.put(MyProject.MyCart.QUANTITY, Integer.valueOf(quantity.getText().toString()));
        db.insert(MyProject.MyCart.TABLE_NAME, null, cv);

        helper.close();
    }
}