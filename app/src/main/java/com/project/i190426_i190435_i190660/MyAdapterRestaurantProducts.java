package com.project.i190426_i190435_i190660;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyAdapterRestaurantProducts.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(allProducts.get(position).getName());
        holder.price.setText(String.valueOf(allProducts.get(position).getPrice()));
        holder.category.setText(String.valueOf(allProducts.get(position).getCategory()));

//        int id=c.getResources().getIdentifier(allProducts.get(position).getPhoto(), "drawable", c.getPackageName());

//        holder.image.setImageResource(id);

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
