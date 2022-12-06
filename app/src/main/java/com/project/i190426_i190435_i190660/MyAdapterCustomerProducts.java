package com.project.i190426_i190435_i190660;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterCustomerProducts extends RecyclerView.Adapter<MyAdapterCustomerProducts.MyViewHolder>{

    List<Product> allProducts;
    Context c;

    public MyAdapterCustomerProducts(List<Product> allProducts, Context c) {
        this.allProducts = allProducts;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterCustomerProducts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.customer_product_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterCustomerProducts.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(allProducts.get(position).getName());
        holder.category.setText(allProducts.get(position).getCategory());
        holder.price.setText(String.valueOf(allProducts.get(position).getPrice()));


//        int id=c.getResources().getIdentifier(allProducts.get(position).getPhoto(), "drawable", c.getPackageName());
//
//        holder.image.setImageResource(id);

        Picasso.get().load(Uri.parse(Ip.ipAdd+"/"+allProducts.get(position).getPhoto())).into(holder.image);


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request1 = new StringRequest(Request.Method.POST,
                        Ip.ipAdd + "/getProductfromCart.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject res = new JSONObject(response);

                                    if (res.getInt("reqcode") == 1) {
                                        JSONArray foodItems=res.getJSONArray("items");
                                        if(foodItems.length()>0){
                                            Toast.makeText(c, "Item is already in Cart", Toast.LENGTH_LONG).show();
                                            Intent intent=new Intent(c, Cart.class);
                                            c.startActivity(intent);
                                        }
                                        else{
                                            Intent intent=new Intent(c, ProductDetail.class);
                                            intent.putExtra("id", allProducts.get(position).getId());
                                            intent.putExtra("name", allProducts.get(position).getName());
                                            intent.putExtra("price", allProducts.get(position).getPrice());
                                            intent.putExtra("image", allProducts.get(position).getPhoto());
                                            intent.putExtra("description", allProducts.get(position).getDescription());
                                            intent.putExtra("category", allProducts.get(position).getCategory());
                                            c.startActivity(intent);

                                        }
                                    }
                                    else {
                                        Toast.makeText(c, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                    }


                                }
                                catch (Exception e){

                                }




                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(c, "Connection Error", Toast.LENGTH_LONG).show();
                                Toast.makeText(c, error.toString(), Toast.LENGTH_LONG).show();

                            }
                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        SharedPreferences mPref;

                        mPref=c.getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);

                        params.put("item_id", String.valueOf(allProducts.get(position).getId()));
                        params.put("customer_id", String.valueOf(mPref.getInt("id", 0)));

                        return params;
                    }
                };

                RequestQueue queue1 = Volley.newRequestQueue(c);
                queue1.add(request1);


            }
        });
    }



    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, category;
        ImageView image;
        Button add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            category=itemView.findViewById(R.id.category);
            image=itemView.findViewById(R.id.image);
            add=itemView.findViewById(R.id.add);


        }
    }
}
