package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {

    List<CartItems> cartItems;
    MyAdapterCart adapter;
    ImageView back;
    RecyclerView rv;
    TextView itemTotal, total, tax;
    double taxCal=20, itemTotalCal=0, totalCal=0;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;
    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back=findViewById(R.id.back);
        cartItems=new ArrayList<>();
        rv=findViewById(R.id.rv);
        itemTotal=findViewById(R.id.itemTotal);
        tax=findViewById(R.id.tax);
        total=findViewById(R.id.total);
        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();
        checkout=findViewById(R.id.checkout);

        adapter=new MyAdapterCart(cartItems, Cart.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(Cart.this);
        rv.setLayoutManager(lm);

        calculatePrice();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();

            }
        });
    }

    public void placeOrder(){

        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/addOrder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {

                                int idOrder=res.getInt("id");

                                for (int i=0; i<cartItems.size(); i++){
                                    addOrderItem(idOrder, cartItems.get(i));
                                    deletefromCartPhp(cartItems.get(i).getP().getId(), mPref.getInt("id", 0));

                                    pushOrderNotification("Order has been successfully placed");

                                    Toast.makeText(Cart.this, "Order Placed", Toast.LENGTH_LONG).show();

                                    Intent intent=new Intent(Cart.this, MainPageCustomer.class);
                                    startActivity(intent);



                                }



                            } else {
                                Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Cart.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                            Toast.makeText(Cart.this, response, Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

                    }


                }) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String dateTime = dtf.format(now);


                params.put("customer_id", String.valueOf(mPref.getInt("id", 0)));
                params.put("dateTime", dateTime);
                params.put("tax", String.valueOf(taxCal));
                params.put("status", "Placed");


                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(Cart.this);
        queue.add(request);

    }

    public void addOrderItem(int id, CartItems c){
        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/addOrderItem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {




                            } else {
                                Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Cart.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                            Toast.makeText(Cart.this, response, Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

                    }


                }) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("order_id", String.valueOf(id));
                params.put("product_id", String.valueOf(c.getP().getId()));
                params.put("quantity", String.valueOf(c.getQuantity()));
                params.put("price", String.valueOf(c.getP().getPrice()));



                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(Cart.this);
        queue.add(request);

    }

    public void deletefromCartPhp(int item_id, int cust_id){
        StringRequest request1 = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/deleteFromcart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {

                            }
                            else {
                                Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (Exception e){

                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("item_id", String.valueOf(item_id));
                params.put("customer_id", String.valueOf(cust_id));

                return params;
            }
        };

        RequestQueue queue1 = Volley.newRequestQueue(Cart.this);
        queue1.add(request1);

    }

    public void calculatePrice(){

        itemTotalCal=0;


        for(int i=0; i<cartItems.size(); i++){
            itemTotalCal+=(cartItems.get(i).getQuantity()*cartItems.get(i).getP().getPrice());
        }

        itemTotal.setText(String.valueOf(itemTotalCal));

        taxCal=5*itemTotalCal/100;

        tax.setText(String.valueOf(taxCal));

        total.setText(String.valueOf(taxCal+itemTotalCal));

    }

    @Override
    protected void onResume() {
        super.onResume();

        StringRequest request = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/getcartItem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                JSONArray foodItems = res.getJSONArray("items");
                                for (int i = 0; i < foodItems.length(); i++) {
                                    JSONObject cartitem = foodItems.getJSONObject(i);
                                    int id = cartitem.getInt("item_id");
                                    int qq = cartitem.getInt("quantity");

                                    StringRequest request1 = new StringRequest(Request.Method.POST,
                                            Ip.ipAdd + "/getProductbyId.php",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject res = new JSONObject(response);

                                                        if (res.getInt("reqcode") == 1) {

                                                            JSONObject item = res.getJSONObject("item");

                                                            Product p = new Product(item.getInt("id"),item.getString("name"), item.getDouble("price"), item.getString("description"),item.getString("photo"), item.getString("category"));

                                                            cartItems.add(new CartItems(p, qq));

                                                            calculatePrice();

                                                            adapter.notifyDataSetChanged();



                                                        } else {
                                                            Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    catch (Exception e){

                                                    }




                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                                                    Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

                                                }
                                            }){
                                        @Nullable
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();

                                            params.put("id", String.valueOf(id));

                                            return params;
                                        }
                                    };

                                    RequestQueue queue1 = Volley.newRequestQueue(Cart.this);
                                    queue1.add(request1);


                                }




                            } else {
                                Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e){

                        }




                        }
                    },
                            new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                            Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

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

                    RequestQueue queue = Volley.newRequestQueue(Cart.this);
                    queue.add(request);




                }

    public void pushOrderNotification(String notif) {

        StringRequest request1 = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/getCustomerbyId.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                JSONObject user=res.getJSONObject("user");

                                try {
                                    JSONObject object = new JSONObject("{ " +
                                            "'include_player_ids': [" + user.getString("deviceId") + "]," +
                                            "'contents': { 'en': '" + notif + "' }," +
                                            "'headers': { 'en': 'Order' }" +
                                            " }");
                                    OneSignal.postNotification(object, new OneSignal.PostNotificationResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject jsonObject) {
//                                            Toast.makeText(Cart.this, "Notification Send", Toast.LENGTH_LONG).show();
                                            System.out.println("Notification Send");

                                        }

                                        @Override
                                        public void onFailure(JSONObject jsonObject) {
//                                            Toast.makeText(Cart.this, "Error Sending Notification", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(Cart.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                                            System.out.println(jsonObject.toString());

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }




                            }
                            else {
                                Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (Exception e){

                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("id", String.valueOf(mPref.getInt("id", 0)));

                return params;
            }
        };

        RequestQueue queue1 = Volley.newRequestQueue(Cart.this);
        queue1.add(request1);


        StringRequest request = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/getAdmin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                JSONArray admins=res.getJSONArray("admins");

                                for (int i=0; i<admins.length(); i++) {
                                    JSONObject u = admins.getJSONObject(i);
                                    try {
                                        JSONObject object1 = new JSONObject("{ " +
                                                "'include_player_ids': [" + u.getString("deviceId") + "]," +
                                                "'contents': { 'en': '" + "New Order" + "' }," +
                                                "'headers': { 'en': 'Order' }" +
                                                " }");

                                        Toast.makeText(Cart.this,  u.getString("deviceId"),Toast.LENGTH_LONG).show();

                                        OneSignal.postNotification(object1, new OneSignal.PostNotificationResponseHandler() {
                                            @Override
                                            public void onSuccess(JSONObject jsonObject) {
//                                            Toast.makeText(Cart.this, "Notification Send", Toast.LENGTH_LONG).show();
                                                System.out.println("Notification Send");

                                            }

                                            @Override
                                            public void onFailure(JSONObject jsonObject) {
//                                            Toast.makeText(Cart.this, "Error Sending Notification", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(Cart.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                                                System.out.println(jsonObject.toString());

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                            else {
                                Toast.makeText(Cart.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (Exception e){

                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cart.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("id", String.valueOf(mPref.getInt("id", 0)));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Cart.this);
        queue1.add(request);



    }
}



//        cartItems.add(new CartItems(new Product(1, "Hamburger", 700,
//                "A simple burger consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll.",
//                "burger", "Burger"), 5));
//
//        cartItems.add(new CartItems(new Product(5, "Vegetable Pizza", 1200,
//                "It has (almost) everything: roasted red peppers, baby spinach, onions, mushrooms, tomatoes, and black olives. It's also topped with three kinds of cheese — feta, provolone, and mozzarella — and sprinkled with garlic herb seasoning. It's an awesome way to eat your vegetables!",
//                "pizza", "Pizza"), 2));