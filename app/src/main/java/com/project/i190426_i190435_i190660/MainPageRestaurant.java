package com.project.i190426_i190435_i190660;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

public class MainPageRestaurant extends AppCompatActivity {

    List<Product> allProducts;
    List<Product> allSearchProducts;
    MyAdapterRestaurantProducts adapter;

    ImageView add, menu;
    TextView addProduct, logout, refresh, offline, orders;
    DrawerLayout drawer;
    RecyclerView rv;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_restaurant);

        allProducts=new ArrayList<>();
        allSearchProducts=new ArrayList<>();

        menu=findViewById(R.id.menu);
        add=findViewById(R.id.add);
        addProduct=findViewById(R.id.addProduct);
        logout=findViewById(R.id.logout);
        drawer=findViewById(R.id.drawer);
        rv=findViewById(R.id.rv);
        orders=findViewById(R.id.orders);
        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();

        search=findViewById(R.id.search);
        offline=findViewById(R.id.offline);
        refresh=findViewById(R.id.refresh);

        adapter=new MyAdapterRestaurantProducts(allProducts, MainPageRestaurant.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPageRestaurant.this);
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
                editmPref.putBoolean("loggedInAdmin", false);
                editmPref.apply();
                editmPref.commit();
                Intent intent=new Intent(MainPageRestaurant.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageRestaurant.this, MainPageRestaurant.class);
                finish();
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPageRestaurant.this, RestaurantOrders.class);
                startActivity(intent);

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        search.setText("");

        allProducts.clear();
        allSearchProducts.clear();

        boolean connected=Ip.isConnected(getApplicationContext());

        if(connected){
            offline.setText("");
            refresh.setText("");
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
                                    Toast.makeText(MainPageRestaurant.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainPageRestaurant.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();

                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainPageRestaurant.this, "Connection Error", Toast.LENGTH_LONG).show();
                        }
                    });

            request1.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue1 = Volley.newRequestQueue(MainPageRestaurant.this);
            queue1.add(request1);

        }
        else{
            offline.setText("You are Offline...");
            refresh.setText("Refresh");
            Toast.makeText(MainPageRestaurant.this, "You are offline", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                loadProductsfromSQlite();
            }
        }






    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadProductsfromSQlite(){

        search.setText("");

        allProducts.clear();
        allSearchProducts.clear();

        MyDBHelper helper= new MyDBHelper(MainPageRestaurant.this);
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

            final String imageData=Base64.getEncoder().encodeToString(c.getBlob(a6));

            Product p=new Product(c.getInt(a1), c.getString(a2), c.getDouble(a3), c.getString(a5), imageData , c.getString(a4));
            allProducts.add(p);
            adapter.notifyDataSetChanged();

        }


    }
}



//        allProducts.add(new Product(1, "Hamburger", 700,
//                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
//                "burger", "Burger"));
//        allProducts.add(new Product(2, "Vegetable Pizza", 1200,
//                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
//                "pizza", "Pizza"));
//        allProducts.add(new Product(3, "Chicken Biryani", 900,
//                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
//                "Rice"));
//
//        allProducts.add(new Product(4, "Hamburger", 700,
//                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
//                "burger", "Burger"));
//        allProducts.add(new Product(5, "Vegetable Pizza", 1200,
//                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
//                "pizza", "Pizza"));
//        allProducts.add(new Product(6, "Chicken Biryani", 900,
//                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
//                "Rice"));