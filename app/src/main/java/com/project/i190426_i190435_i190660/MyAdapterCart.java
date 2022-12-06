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

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.MyViewHolder>{
    List<CartItems> list;
    Context c;

    public MyAdapterCart(List<CartItems> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.cart_item_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterCart.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getP().getName());
        holder.price.setText("Rs. "+String.valueOf(list.get(position).getP().getPrice()));
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.totalPrice.setText("Rs. "+String.valueOf(list.get(position).getP().getPrice()*list.get(position).getQuantity()));

//        int id=c.getResources().getIdentifier(list.get(position).getP().getPhoto(), "drawable", c.getPackageName());
//
//        holder.image.setImageResource(id);

        Picasso.get().load(Uri.parse(Ip.ipAdd+"/"+list.get(position).getP().getPhoto())).into(holder.image);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(holder.quantity.getText().toString());
                q=q+1;
                list.get(position).setQuantity(q);
                notifyDataSetChanged();
                if (c instanceof Cart) {
                    ((Cart)c).calculatePrice();
                }


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(holder.quantity.getText().toString());
                if(q>1){
                    q=q-1;
                    list.get(position).setQuantity(q);
                    notifyDataSetChanged();
                    if (c instanceof Cart) {
                        ((Cart)c).calculatePrice();
                    }

                }

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mPref;

                mPref=c.getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
                deletefromPhp(list.get(position).getP().getId(), mPref.getInt("id", 0));
                list.remove(position);
                notifyDataSetChanged();
                if (c instanceof Cart) {
                    ((Cart)c).calculatePrice();
                }



            }
        });

    }

    public void deletefromPhp(int item_id, int cust_id){
        StringRequest request1 = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/deleteFromcart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                Toast.makeText(c, "Deleted", Toast.LENGTH_LONG).show();
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


                params.put("item_id", String.valueOf(item_id));
                params.put("customer_id", String.valueOf(cust_id));

                return params;
            }
        };

        RequestQueue queue1 = Volley.newRequestQueue(c);
        queue1.add(request1);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, totalPrice, quantity;
        ImageView image;
        ImageButton plus, minus, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            quantity=itemView.findViewById(R.id.quantity);
            image=itemView.findViewById(R.id.image);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
