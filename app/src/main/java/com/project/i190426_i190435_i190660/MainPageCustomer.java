package com.project.i190426_i190435_i190660;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

public class MainPageCustomer extends AppCompatActivity {

    List<Product> allProducts;
    List<Product> allSearchProducts;
    MyAdapterCustomerProducts adapter;

    ImageView cart, menu;
    TextView logout, profile, prevOrders, cartItems,  offline, refresh;
    DrawerLayout drawer;
    RecyclerView rv;
    EditText search;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_customer);

        allProducts=new ArrayList<>();
        allSearchProducts=new ArrayList<>();

        menu=findViewById(R.id.menu);
        cart=findViewById(R.id.cart);
        cartItems=findViewById(R.id.cartItems);
        profile=findViewById(R.id.profile);
        prevOrders=findViewById(R.id.prevOrders);
        logout=findViewById(R.id.logout);
        drawer=findViewById(R.id.drawer);
        rv=findViewById(R.id.rv);
        search=findViewById(R.id.search);
        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();
        offline=findViewById(R.id.offline);
        refresh=findViewById(R.id.refresh);




        adapter=new MyAdapterCustomerProducts(allProducts, MainPageCustomer.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPageCustomer.this);
        rv.setLayoutManager(lm);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allProducts.clear();

                for(int a=0; a<allSearchProducts.size(); a++){
                    if(allSearchProducts.get(a).getName().toLowerCase(Locale.ROOT).contains(search.getText().toString().toLowerCase(Locale.ROOT))){
                        allProducts.add(allSearchProducts.get(a));
                    }
                    else if(allSearchProducts.get(a).getCategory().toLowerCase(Locale.ROOT).contains(search.getText().toString().toLowerCase(Locale.ROOT))){
                        allProducts.add(allSearchProducts.get(a));
                    }
                    else if(allSearchProducts.get(a).getDescription().toLowerCase(Locale.ROOT).contains(search.getText().toString().toLowerCase(Locale.ROOT))){
                        allProducts.add(allSearchProducts.get(a));
                    }
                    adapter.notifyDataSetChanged();
                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
                editmPref.putBoolean("loggedInCustomer", false);
                editmPref.apply();
                editmPref.commit();
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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageCustomer.this, MainPageCustomer.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();



        boolean connected = Ip.isConnected(getApplicationContext());



        if(connected){
            offline.setText("");
            refresh.setText("");
            loadProductsfromPhp();

        }
        else{
            offline.setText("You are Offline...");
            refresh.setText("Refresh");
            Toast.makeText(MainPageCustomer.this, "You are offline", Toast.LENGTH_LONG).show();

                loadProductsfromSQlite();
        }

    }



    public void loadProductsfromPhp(){
        search.setText("");

        allProducts.clear();
        allSearchProducts.clear();

        StringRequest request1 = new StringRequest(Request.Method.GET, Ip.ipAdd + "/getFoodItems.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);


                        try {
                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                JSONArray foodItems=res.getJSONArray("items");
                                for (int i=0; i<foodItems.length(); i++){
                                    JSONObject u=foodItems.getJSONObject(i);

                                    allProducts.add(new Product(u.getInt("id"),u.getString("name"), u.getDouble("price"), u.getString("description"),u.getString("photo"), u.getString("category")));
                                    allSearchProducts.add(new Product(u.getInt("id"),u.getString("name"), u.getDouble("price"), u.getString("description"),u.getString("photo"), u.getString("category")));
                                    adapter.notifyDataSetChanged();


                                }



                            } else {
                                Toast.makeText(MainPageCustomer.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainPageCustomer.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainPageCustomer.this, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        request1.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue1 = Volley.newRequestQueue(MainPageCustomer.this);
        queue1.add(request1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadProductsfromSQlite(){
        search.setText("");

        allProducts.clear();
        allSearchProducts.clear();


        MyDBHelper helper= new MyDBHelper(MainPageCustomer.this);
        SQLiteDatabase db= helper.getReadableDatabase();
        String[] cols= {MyProject.MyProducts._ID,
                MyProject.MyProducts._NAME,
                MyProject.MyProducts._PRICE,
                MyProject.MyProducts._CATEGORY,
                MyProject.MyProducts._DESCRIPTION,
                MyProject.MyProducts._PHOTO};
        Cursor c=db.query(
                MyProject.MyProducts.TABLE_NAME,
                cols,
                null,
                null,
                null,
                null,
                MyProject.MyProducts._ID+" DESC"
        );
        while(c.moveToNext()){
            int a1= c.getColumnIndex(MyProject.MyProducts._ID);
            int a2= c.getColumnIndex(MyProject.MyProducts._NAME);
            int a3= c.getColumnIndex(MyProject.MyProducts._PRICE);
            int a4= c.getColumnIndex(MyProject.MyProducts._CATEGORY);
            int a5= c.getColumnIndex(MyProject.MyProducts._DESCRIPTION);
            int a6= c.getColumnIndex(MyProject.MyProducts._PHOTO);

            final String imageData= Base64.getEncoder().encodeToString(c.getBlob(a6));

            Product p=new Product(c.getInt(a1), c.getString(a2), c.getDouble(a3), c.getString(a5), imageData , c.getString(a4));
            allProducts.add(p);
            adapter.notifyDataSetChanged();

        }



    }
}