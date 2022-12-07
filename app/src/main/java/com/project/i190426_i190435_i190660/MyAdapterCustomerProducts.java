package com.project.i190426_i190435_i190660;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyAdapterCustomerProducts.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(allProducts.get(position).getName());
        holder.category.setText(allProducts.get(position).getCategory());
        holder.price.setText(String.valueOf(allProducts.get(position).getPrice()));



        if(Ip.isConnected(c)){
            Picasso.get().load(Uri.parse(Ip.ipAdd+"/"+allProducts.get(position).getPhoto())).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.image.setImageBitmap(bitmap);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        checkProductinSqlite(allProducts.get(position), byteArray);


                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });




        }
        else {
            byte[] imageData= Base64.getDecoder().decode(allProducts.get(position).getPhoto());
            Bitmap dppp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            holder.image.setImageBitmap(dppp);
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Ip.isConnected(c)) {
                    StringRequest request1 = new StringRequest(Request.Method.POST,
                            Ip.ipAdd + "/getProductfromCart.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject res = new JSONObject(response);

                                        if (res.getInt("reqcode") == 1) {
                                            JSONArray foodItems = res.getJSONArray("items");
                                            if (foodItems.length() > 0) {
                                                Toast.makeText(c, "Item is already in Cart", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(c, Cart.class);
                                                c.startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(c, ProductDetail.class);
                                                intent.putExtra("id", allProducts.get(position).getId());
                                                intent.putExtra("name", allProducts.get(position).getName());
                                                intent.putExtra("price", allProducts.get(position).getPrice());
                                                intent.putExtra("image", allProducts.get(position).getPhoto());
                                                intent.putExtra("description", allProducts.get(position).getDescription());
                                                intent.putExtra("category", allProducts.get(position).getCategory());
                                                c.startActivity(intent);

                                            }
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

                            SharedPreferences mPref;

                            mPref = c.getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);

                            params.put("item_id", String.valueOf(allProducts.get(position).getId()));
                            params.put("customer_id", String.valueOf(mPref.getInt("id", 0)));

                            return params;
                        }
                    };

                    RequestQueue queue1 = Volley.newRequestQueue(c);
                    queue1.add(request1);
                }
                else {
                    Toast.makeText(c, "You are offline", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void checkProductinSqlite(Product p, byte[] byteArray){

        boolean present=false;

        MyDBHelper helper= new MyDBHelper(c);
        SQLiteDatabase db= helper.getReadableDatabase();
        String[] cols= {MyProject.MyProducts._ID,
                MyProject.MyProducts._NAME,
                MyProject.MyProducts._PRICE,
                MyProject.MyProducts._CATEGORY,
                MyProject.MyProducts._DESCRIPTION,
                MyProject.MyProducts._PHOTO};
        Cursor c1=db.query(
                MyProject.MyProducts.TABLE_NAME,
                cols,
                MyProject.MyProducts._ID+"="+p.getId(),
                null,
                null,
                null,
                MyProject.MyProducts._ID+" DESC"
        );
        while(c1.moveToNext()){
            present=true;
        }

        if(present==false){


            MyDBHelper helper1= new MyDBHelper(c);
            SQLiteDatabase db1 = helper1.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(MyProject.MyProducts._ID, p.getId());
            cv.put(MyProject.MyProducts._NAME, p.getName());
            cv.put(MyProject.MyProducts._PRICE, p.getPrice());
            cv.put(MyProject.MyProducts._CATEGORY, p.getCategory());
            cv.put(MyProject.MyProducts._DESCRIPTION, p.getDescription());
            cv.put(MyProject.MyProducts._PHOTO, byteArray);
            db.insert(MyProject.MyProducts.TABLE_NAME, null, cv);

            helper.close();
        }
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
