package com.project.i190426_i190435_i190660;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyAdapterRestaurantOrder extends RecyclerView.Adapter<MyAdapterRestaurantOrder.MyViewHolder>{

    List<Order> orders;
    Context c;

    public MyAdapterRestaurantOrder(List<Order> orders, Context c) {
        this.orders = orders;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.restaurant_order_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.date.setText(orders.get(position).getDate());
        double total=0;
        for(int i=0; i<orders.get(position).getOrderItemList().size(); i++){
            total+=orders.get(position).getOrderItemList().get(i).getQuantity()*orders.get(position).getOrderItemList().get(i).getPrice();
        }

        total+=orders.get(position).getTax();

        holder.total.setText("Rs. "+String.valueOf(total));

        MyAdapterOrderItem adapterOrderItem;
        List<OrderItem> orderedItems=new ArrayList<>();

        for(int i=0; i<orders.get(position).getOrderItemList().size(); i++){
            orderedItems.add(orders.get(position).getOrderItemList().get(i));
        }

        adapterOrderItem=new MyAdapterOrderItem(orderedItems, c);
        holder.rv.setAdapter(adapterOrderItem);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(c);
        holder.rv.setLayoutManager(lm);

        if(Ip.isConnected(c.getApplicationContext())) {


            StringRequest request = new StringRequest(Request.Method.POST,
                    Ip.ipAdd + "/getOrderbyId.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject res = new JSONObject(response);

                                if (res.getInt("reqcode") == 1) {

                                    JSONObject order = res.getJSONObject("orders");

                                    int cust_id = order.getInt("customer_id");


                                    List<OrderItem> orderItemList = new ArrayList<>();

                                    StringRequest request1 = new StringRequest(Request.Method.POST,
                                            Ip.ipAdd + "/getCustomerbyId.php",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {

                                                        JSONObject res = new JSONObject(response);

                                                        if (res.getInt("reqcode") == 1) {
                                                            JSONObject user = res.getJSONObject("user");
                                                            holder.customer.setText(user.getString("name") + "(" + user.getString("email") + ")");


                                                        } else {
                                                            Toast.makeText(c, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                                        }


                                                    } catch (Exception e) {

                                                    }


                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(c, "Connection Error", Toast.LENGTH_LONG).show();
                                                    Toast.makeText(c, error.toString(), Toast.LENGTH_LONG).show();

                                                }
                                            }) {
                                        @Nullable
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();


                                            params.put("id", String.valueOf(cust_id));

                                            return params;
                                        }
                                    };

                                    RequestQueue queue1 = Volley.newRequestQueue(c);
                                    queue1.add(request1);


                                } else {
                                    Toast.makeText(c, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(c, "Connection Error", Toast.LENGTH_LONG).show();
                            Toast.makeText(c, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("order_id", String.valueOf(orders.get(position).getId()));

                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(c);
            queue.add(request);

        }
        else{

        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView total, date, customer;
        RecyclerView rv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            total=itemView.findViewById(R.id.total);
            rv=itemView.findViewById(R.id.rv);
            date=itemView.findViewById(R.id.date);
            customer=itemView.findViewById(R.id.customer);


        }
    }
}
