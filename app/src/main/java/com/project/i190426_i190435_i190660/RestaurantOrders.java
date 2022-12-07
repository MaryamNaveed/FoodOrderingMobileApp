package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantOrders extends AppCompatActivity {

    ImageView back;
    RecyclerView rv;
    List<Order> orders;
    MyAdapterRestaurantOrder adapter;

    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_orders);
        back=findViewById(R.id.back);
        rv=findViewById(R.id.rv);

        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();

//        List<OrderItem> cartItems1=new ArrayList<>();

//        Product p1=new Product(1, "Hamburger", 700,
//                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
//                "burger", "Burger");
//
//        cartItems1.add(new OrderItem(p1, 5, p1.price));
//
//        Product p2=new Product(5, "Vegetable Pizza", 1200,
//                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
//                "pizza", "Pizza");
//
//        cartItems1.add(new OrderItem(p2, 2, p2.price));
//
//        List<OrderItem> cartItems2=new ArrayList<>();
//
//        Product p3=new Product(6, "Chicken Biryani", 900,
//                "A savory chicken and rice dish that includes layers of chicken, rice, and aromatics that are steamed together. The bottom layer of rice absorbs all the chicken juices as it cooks, giving it a tender texture and rich flavor, while the top layer of rice turns out white and fluffy.", "biryani",
//                "Rice");
//
//        cartItems2.add(new OrderItem(p3, 5, p3.price));
//
//        Product p4=new Product(5, "Vegetable Pizza", 1200,
//                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
//                "pizza", "Pizza");
//
//        cartItems2.add(new OrderItem(p4, 2, p4.price));

        orders=new ArrayList<>();


//        orders.add(new Order(cartItems1, "25 July 2022", 20));
//        orders.add(new Order(cartItems2, "25 August 2022", 20));

        adapter=new MyAdapterRestaurantOrder(orders, RestaurantOrders.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(RestaurantOrders.this);
        rv.setLayoutManager(lm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        StringRequest request = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/getOrders.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                JSONArray myorders = res.getJSONArray("orders");
//                                Toast.makeText(PreviousOrders.this, String.valueOf(myorders.length()), Toast.LENGTH_LONG).show();
                                for (int i = 0; i < myorders.length(); i++) {
                                    JSONObject order = myorders.getJSONObject(i);
                                    int id = order.getInt("order_id");
                                    int cust_id = order.getInt("customer_id");
                                    double tax = order.getInt("tax");
                                    String dateTime = order.getString("dateTime");
                                    String status = order.getString("status");

                                    List<OrderItem> orderItemList=new ArrayList<>();

                                    StringRequest request2 = new StringRequest(Request.Method.POST,
                                            Ip.ipAdd + "/getOrderItembyId.php",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject res = new JSONObject(response);

                                                        if (res.getInt("reqcode") == 1) {
                                                            JSONArray orderitems = res.getJSONArray("orderitems");
                                                            for (int i = 0; i < orderitems.length(); i++) {
                                                                JSONObject item = orderitems.getJSONObject(i);

                                                                int item_id = item.getInt("product_id");
                                                                double price = item.getDouble("price");
                                                                int quantity = item.getInt("quantity");

                                                                StringRequest request1 = new StringRequest(Request.Method.POST,
                                                                        Ip.ipAdd + "/getProductbyId.php",
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {

                                                                                System.out.println(response);

                                                                                try {
                                                                                    JSONObject res = new JSONObject(response);

                                                                                    if (res.getInt("reqcode") == 1) {

                                                                                        JSONObject item = res.getJSONObject("item");

                                                                                        Product p = new Product(item.getInt("id"),item.getString("name"), item.getDouble("price"), item.getString("description"),item.getString("photo"), item.getString("category"));

                                                                                        orderItemList.add(new OrderItem(p, quantity, price));


                                                                                    } else {
                                                                                        Toast.makeText(RestaurantOrders.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                                catch (Exception e){

                                                                                }




                                                                            }
                                                                        },
                                                                        new Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                Toast.makeText(RestaurantOrders.this, "Connection Error", Toast.LENGTH_LONG).show();
                                                                                Toast.makeText(RestaurantOrders.this, error.toString(), Toast.LENGTH_LONG).show();

                                                                            }
                                                                        }){
                                                                    @Nullable
                                                                    @Override
                                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                                        Map<String, String> params = new HashMap<>();

                                                                        params.put("id", String.valueOf(item_id));

                                                                        return params;
                                                                    }
                                                                };

                                                                RequestQueue queue1 = Volley.newRequestQueue(RestaurantOrders.this);
                                                                queue1.add(request1);



                                                            }
                                                            orders.add(new Order(id, orderItemList, dateTime, tax, status));
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                        else{
                                                            Toast.makeText(RestaurantOrders.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();


                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(RestaurantOrders.this, "Connection Error", Toast.LENGTH_LONG).show();
                                                    Toast.makeText(RestaurantOrders.this, error.toString(), Toast.LENGTH_LONG).show();

                                                }
                                            }){
                                        @Nullable
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();

                                            params.put("order_id", String.valueOf(id));

                                            return params;
                                        }
                                    };

                                    RequestQueue queue2 = Volley.newRequestQueue(RestaurantOrders.this);
                                    queue2.add(request2);




                                }

                            } else {
                                Toast.makeText(RestaurantOrders.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RestaurantOrders.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(RestaurantOrders.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("customer_id", String.valueOf(mPref.getInt("id", 0)));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(RestaurantOrders.this);
        queue.add(request);

    }
}