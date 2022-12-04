package com.project.i190426_i190435_i190660;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterRestaurantProducts extends RecyclerView.Adapter<MyAdapterRestaurantProducts.MyViewHolder>{

    List<Product> allProducts;
    Context c;

    public MyAdapterRestaurantProducts(List<Product> allProducts, Context c) {
        this.allProducts = allProducts;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterRestaurantProducts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.restaurant_product_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterRestaurantProducts.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(allProducts.get(position).getName());
        holder.price.setText(String.valueOf(allProducts.get(position).getPrice()));
        holder.category.setText(String.valueOf(allProducts.get(position).getCategory()));

//        int id=c.getResources().getIdentifier(allProducts.get(position).getPhoto(), "drawable", c.getPackageName());

//        holder.image.setImageResource(id);

        Picasso.get().load(Uri.parse(Ip.ipAdd+"/"+allProducts.get(position).getPhoto())).into(holder.image);




        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, UpdateProduct.class);
                intent.putExtra("id", allProducts.get(position).getId());
                intent.putExtra("name", allProducts.get(position).getName());
                intent.putExtra("price", allProducts.get(position).getPrice());
                intent.putExtra("image", allProducts.get(position).getPhoto());
                intent.putExtra("description", allProducts.get(position).getDescription());
                intent.putExtra("category", allProducts.get(position).getCategory());
                c.startActivity(intent);
            }
        });



    }

    public  void deleteProduct(int idd){
        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/deleteProduct.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res1 = new JSONObject(response);

                            if(res1.getInt("reqcode")==1){
                                Toast.makeText(c, "Foot Item Deleted", Toast.LENGTH_LONG).show();


                            }
                            else {
                                Toast.makeText(c, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(c, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
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


                params.put("id", String.valueOf(idd));




                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price,category;
        ImageView image;
//        ImageButton update, delete;

        Button update;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            category=itemView.findViewById(R.id.category);
            image=itemView.findViewById(R.id.image);
            update=itemView.findViewById(R.id.update);
//            delete=itemView.findViewById(R.id.delete);


        }
    }
}
