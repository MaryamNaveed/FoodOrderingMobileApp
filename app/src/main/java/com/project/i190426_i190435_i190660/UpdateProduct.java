package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateProduct extends AppCompatActivity {

    Product product;
    EditText name, price, description, category;
    ImageView image, back;
    Button update;
    Uri selectedImage= null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);
        category=findViewById(R.id.category);
        image=findViewById(R.id.image);
        back=findViewById(R.id.back);
        update=findViewById(R.id.update);



        product=new Product(
                getIntent().getIntExtra("id", 0),
                getIntent().getStringExtra("name"),
                getIntent().getDoubleExtra("price", 0),
                getIntent().getStringExtra("description"),
                getIntent().getStringExtra("image"),
                getIntent().getStringExtra("category"));

        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        description.setText(product.getDescription());
        category.setText(product.getCategory());
//        int id=getResources().getIdentifier(product.getPhoto(), "drawable", getPackageName());
//        image.setImageResource(id);

        Picasso.get().load(Uri.parse(Ip.ipAdd+"/"+product.getPhoto())).into(image);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(i, "Choose your Image"),
                        200
                );

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

                if(connected){


                    updateProducttophp();
                }
                else {
                    Toast.makeText(UpdateProduct.this, "You are offline", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            image.setImageURI(selectedImage);
        }
    }

    public void uploadImage(){

        VolleyMultipartRequest request= new VolleyMultipartRequest(Request.Method.POST, Ip.ipAdd + "/uploadImage.php",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if(obj.getInt("code")==1){
                                String url=obj.getString("url");
                                Toast.makeText(UpdateProduct.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                update(url);
                            }
                            else{
                                Toast.makeText(UpdateProduct.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateProduct.this, "Could not parse JSON", Toast.LENGTH_SHORT).show();
                            Toast.makeText(UpdateProduct.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateProduct.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        Toast.makeText(UpdateProduct.this, error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                Bitmap bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
                params.put("dp", new DataPart("foodItem" + imagename + ".png", getFileDataFromDrawable(bmp)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);


    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void update(String url){

        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/updateProduct.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res1 = new JSONObject(response);

                            if(res1.getInt("reqcode")==1){
                                Toast.makeText(UpdateProduct.this, "Foot Item Updated", Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(UpdateProduct.this, MainPageRestaurant.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(UpdateProduct.this, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateProduct.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateProduct.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(UpdateProduct.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", String.valueOf(product.getId()));
                params.put("name", name.getText().toString());
                params.put("price", price.getText().toString());
                params.put("category", category.getText().toString());
                params.put("description", description.getText().toString());
                params.put("photo", url);


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(UpdateProduct.this);
        queue.add(request);

    }

    public void updateProducttophp(){


        uploadImage();
    }
}