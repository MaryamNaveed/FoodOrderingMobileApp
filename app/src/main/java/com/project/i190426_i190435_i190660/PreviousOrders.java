package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviousOrders extends AppCompatActivity {

    ImageView back;
    RecyclerView rv;
    List<Order> orders;
    MyAdapterOrder adapter;

    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);
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

        adapter=new MyAdapterOrder(orders, PreviousOrders.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(PreviousOrders.this);
        rv.setLayoutManager(lm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        orders.clear();

        if(Ip.isConnected(getApplicationContext())) {

            StringRequest request = new StringRequest(Request.Method.POST,
                    Ip.ipAdd + "/getOrderCust.php",
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

                                        List<OrderItem> orderItemList = new ArrayList<>();

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

                                                                    final int a=i;

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

                                                                                            Product p = new Product(item.getInt("id"), item.getString("name"), item.getDouble("price"), item.getString("description"), item.getString("photo"), item.getString("category"));

                                                                                            orderItemList.add(new OrderItem(p, quantity, price));

                                                                                            if(a==orderitems.length()-1){
                                                                                                Toast.makeText(PreviousOrders.this, String.valueOf(orderItemList.size()), Toast.LENGTH_SHORT).show();
                                                                                                orders.add(new Order(id, orderItemList, dateTime, tax, status));
                                                                                                Order o = new Order(id, orderItemList, dateTime, tax, status);


                                                                                                adapter.notifyDataSetChanged();
                                                                                                checkOrderinSqlite(o);
                                                                                            }




                                                                                        } else {
                                                                                            Toast.makeText(PreviousOrders.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                                                                        }
                                                                                    } catch (Exception e) {

                                                                                    }


                                                                                }
                                                                            },
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    Toast.makeText(PreviousOrders.this, "Connection Error", Toast.LENGTH_LONG).show();
                                                                                    Toast.makeText(PreviousOrders.this, error.toString(), Toast.LENGTH_LONG).show();

                                                                                }
                                                                            }) {
                                                                        @Nullable
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            Map<String, String> params = new HashMap<>();

                                                                            params.put("id", String.valueOf(item_id));

                                                                            return params;
                                                                        }
                                                                    };

                                                                    RequestQueue queue1 = Volley.newRequestQueue(PreviousOrders.this);
                                                                    queue1.add(request1);


                                                                }



                                                            } else {
                                                                Toast.makeText(PreviousOrders.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();


                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(PreviousOrders.this, "Connection Error", Toast.LENGTH_LONG).show();
                                                        Toast.makeText(PreviousOrders.this, error.toString(), Toast.LENGTH_LONG).show();

                                                    }
                                                }) {
                                            @Nullable
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();

                                                params.put("order_id", String.valueOf(id));

                                                return params;
                                            }
                                        };

                                        RequestQueue queue2 = Volley.newRequestQueue(PreviousOrders.this);
                                        queue2.add(request2);


                                    }


                                } else {
                                    Toast.makeText(PreviousOrders.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PreviousOrders.this, "Connection Error", Toast.LENGTH_LONG).show();
                            Toast.makeText(PreviousOrders.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("customer_id", String.valueOf(mPref.getInt("id", 0)));

                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(PreviousOrders.this);
            queue.add(request);
        }
        else{
            loadfromSqlite();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadfromSqlite(){

        MyDBHelper helper= new MyDBHelper(PreviousOrders.this);
        SQLiteDatabase db= helper.getReadableDatabase();
        String[] cols= {MyProject.MyOrder._ID,
                MyProject.MyOrder._CUST_ID,
                MyProject.MyOrder._DATETIME,
                MyProject.MyOrder._TAX,
                MyProject.MyOrder._STATUS};
        Cursor c=db.query(
                MyProject.MyOrder.TABLE_NAME,
                cols,
                MyProject.MyOrder._CUST_ID+"="+mPref.getInt("id", 0),
                null,
                null,
                null,
                MyProject.MyOrder._ID+" DESC"
        );
        while(c.moveToNext()){
            int a1= c.getColumnIndex(MyProject.MyOrder._ID);
            int a1_2 = c.getColumnIndex(MyProject.MyOrder._DATETIME);
            int a1_3 = c.getColumnIndex(MyProject.MyOrder._TAX);
            int a1_4 = c.getColumnIndex(MyProject.MyOrder._STATUS);


            List<OrderItem> orderitems=new ArrayList<>();

            MyDBHelper helper1= new MyDBHelper(PreviousOrders.this);
            SQLiteDatabase db1= helper1.getReadableDatabase();
            String[] cols1= {MyProject.MyOrderItem._ID,
                    MyProject.MyOrderItem.ORDER_ID,
                    MyProject.MyOrderItem.ITEM_ID,
                    MyProject.MyOrderItem._QUANTITY,
                    MyProject.MyOrderItem._PRICE};
            Cursor c1=db1.query(
                    MyProject.MyOrderItem.TABLE_NAME,
                    cols1,
                    MyProject.MyOrderItem.ORDER_ID+"="+c.getInt(a1),
                    null,
                    null,
                    null,
                    MyProject.MyOrderItem.ORDER_ID+" DESC"
            );
            while(c1.moveToNext()){
                int a2= c1.getColumnIndex(MyProject.MyOrderItem.ITEM_ID);
                int a3= c1.getColumnIndex(MyProject.MyOrderItem._QUANTITY);
                int a4= c1.getColumnIndex(MyProject.MyOrderItem._PRICE);


                MyDBHelper helper2= new MyDBHelper(PreviousOrders.this);
                SQLiteDatabase db2= helper2.getReadableDatabase();
                String[] cols2= {MyProject.MyProducts._ID,
                        MyProject.MyProducts._NAME,
                        MyProject.MyProducts._PRICE,
                        MyProject.MyProducts._CATEGORY,
                        MyProject.MyProducts._DESCRIPTION,
                        MyProject.MyProducts._PHOTO};
                Cursor c2=db2.query(
                        MyProject.MyProducts.TABLE_NAME,
                        cols2,
                        "id="+c1.getInt(a2),
                        null,
                        null,
                        null,
                        MyProject.MyProducts._ID+" DESC"
                );
                while(c2.moveToNext()){
                    int a11= c2.getColumnIndex(MyProject.MyProducts._ID);
                    int a12= c2.getColumnIndex(MyProject.MyProducts._NAME);
                    int a13= c2.getColumnIndex(MyProject.MyProducts._PRICE);
                    int a14= c2.getColumnIndex(MyProject.MyProducts._CATEGORY);
                    int a15= c2.getColumnIndex(MyProject.MyProducts._DESCRIPTION);
                    int a16= c2.getColumnIndex(MyProject.MyProducts._PHOTO);

                    final String imageData=Base64.getEncoder().encodeToString(c2.getBlob(a16));

                    Product p=new Product(c2.getInt(a11), c2.getString(a12), c2.getDouble(a13), c2.getString(a15), imageData , c2.getString(a14));

                    orderitems.add(new OrderItem(p, c1.getInt(a3), c1.getDouble(a4)));




                }







            }

            orders.add(new Order(c.getInt(a1),orderitems, c.getString(a1_2), c.getInt(a1_3), c.getString(a1_4)));

            adapter.notifyDataSetChanged();


        }

    }

    public void checkOrderinSqlite(Order o) {

        boolean present = false;

        MyDBHelper helper1= new MyDBHelper(PreviousOrders.this);
        SQLiteDatabase db1= helper1.getReadableDatabase();
        String[] cols= { MyProject.MyOrder._ID,
                MyProject.MyOrder._CUST_ID,
                MyProject.MyOrder._DATETIME,
                MyProject.MyOrder._STATUS,
                MyProject.MyOrder._TAX};
        Cursor c1=db1.query(
                MyProject.MyOrder.TABLE_NAME,
                cols,
                MyProject.MyOrder._ID+"="+o.getId(),
                null,
                null,
                null,
                MyProject.MyOrder._ID+" DESC"
        );
        while(c1.moveToNext()){
            present=true;
        }

        if(present==false){
            placeOrderToSqlite(o.getId(), o.getDate(), o.getTax(), o.getStatus());

            for (int i=0; i<o.getOrderItemList().size(); i++){
                addOrderItemtoSqlite(o.getId(), o.getOrderItemList().get(i));
            }

        }
    }

    public void placeOrderToSqlite(int id, String dateTime, double taxCal, String st){


        MyDBHelper helper = new MyDBHelper(PreviousOrders.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyProject.MyOrder._ID, id);
        cv.put(MyProject.MyOrder._CUST_ID, mPref.getInt("id", 0));
        cv.put(MyProject.MyOrder._DATETIME, dateTime);
        cv.put(MyProject.MyOrder._TAX, taxCal);
        cv.put(MyProject.MyOrder._STATUS, st);
        db.insert(MyProject.MyOrder.TABLE_NAME, null, cv);

        helper.close();

    }

    public void addOrderItemtoSqlite(int orderid, OrderItem c){
        MyDBHelper helper = new MyDBHelper(PreviousOrders.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyProject.MyOrderItem.ORDER_ID, orderid);
        cv.put(MyProject.MyOrderItem.ITEM_ID, c.getP().getId());
        cv.put(MyProject.MyOrderItem._QUANTITY, c.getQuantity());
        cv.put(MyProject.MyOrderItem._PRICE, c.getPrice());

        db.insert(MyProject.MyOrderItem.TABLE_NAME, null, cv);

        helper.close();

    }
}