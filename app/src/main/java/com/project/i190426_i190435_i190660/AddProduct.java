package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    EditText name, price, description, category;
    ImageView image, back;
    Button add;
    Uri selectedImage= null;
    String url="";
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);
        category=findViewById(R.id.category);
        image=findViewById(R.id.image);
        back=findViewById(R.id.back);
        add=findViewById(R.id.add);



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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedImage==null){
                    Toast.makeText(AddProduct.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
                else {

                    boolean connected = Ip.isConnected(getApplicationContext());

                    if(connected){
                        uploadImageandDatatophp();

                    }
                    else {
                        Toast.makeText(AddProduct.this, "You are offline", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    public void uploadImageandDatatoSqlite(){

        Bitmap bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        MyDBHelper helper = new MyDBHelper(AddProduct.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyProject.MyProducts._ID, id);
        cv.put(MyProject.MyProducts._NAME, name.getText() + "");
        cv.put(MyProject.MyProducts._PRICE, Double.valueOf(price.getText().toString()));
        cv.put(MyProject.MyProducts._CATEGORY, category.getText() + "");
        cv.put(MyProject.MyProducts._DESCRIPTION, description.getText() + "");
        cv.put(MyProject.MyProducts._PHOTO, byteArray);
        db.insert(MyProject.MyProducts.TABLE_NAME, null, cv);

        helper.close();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            image.setImageURI(selectedImage);
        }
    }

    public void uploadImageandDatatophp(){

        VolleyMultipartRequest request= new VolleyMultipartRequest(Request.Method.POST, Ip.ipAdd + "/uploadImage.php",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if(obj.getInt("code")==1){
                                url=obj.getString("url");
                                Toast.makeText(AddProduct.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                addProduct();
                            }
                            else{
                                Toast.makeText(AddProduct.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddProduct.this, "Could not parse JSON", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddProduct.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddProduct.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddProduct.this, error.toString(), Toast.LENGTH_SHORT).show();


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

    public void addProduct(){

        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/addProduct.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res1 = new JSONObject(response);

                            if(res1.getInt("reqcode")==1){
                                Toast.makeText(AddProduct.this, "Foot Item Added", Toast.LENGTH_LONG).show();

                                id = res1.getInt("id");

                                uploadImageandDatatoSqlite();
                                Intent intent=new Intent(AddProduct.this, MainPageRestaurant.class);
                                startActivity(intent);


                            }
                            else {
                                Toast.makeText(AddProduct.this, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddProduct.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddProduct.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(AddProduct.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", name.getText().toString());
                params.put("price", price.getText().toString());
                params.put("category", category.getText().toString());
                params.put("description", description.getText().toString());
                params.put("photo", url);




                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(AddProduct.this);
        queue.add(request);
        

    }


}
