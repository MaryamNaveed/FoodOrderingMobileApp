package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignupCustomer extends AppCompatActivity {

    TextView show, signin;
    EditText email, password, name, phone;
    Button signup;
    boolean showed=false;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_customer);

        show=findViewById(R.id.show);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);
        editmPref=mPref.edit();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showed==false){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showed=true;
                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showed=false;

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/signupCustomer.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                System.out.println(response);


                                try {
                                    JSONObject res = new JSONObject(response);

                                    if (res.getInt("reqcode") == 1) {

                                        int idUser=res.getInt("id");

                                        Toast.makeText(SignupCustomer.this, "Signed Up", Toast.LENGTH_LONG).show();
                                        editmPref.putBoolean("loggedInAdmin", false);
                                        editmPref.putBoolean("loggedInCustomer", true);
                                        editmPref.putInt("id", idUser);
                                        editmPref.apply();
                                        editmPref.commit();

                                        insertCustomerToSqlite();

                                        Intent intent=new Intent(SignupCustomer.this, MainPageCustomer.class);
                                        startActivity(intent);
                                        finish();


                                    } else {
                                        Toast.makeText(SignupCustomer.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(SignupCustomer.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                                    Toast.makeText(SignupCustomer.this, response, Toast.LENGTH_LONG).show();
                                    System.out.println(response);
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SignupCustomer.this, "Connection Error", Toast.LENGTH_LONG).show();
                                Toast.makeText(SignupCustomer.this, error.toString(), Toast.LENGTH_LONG).show();

                            }


                        }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("name", name.getText().toString());
                        params.put("phone", phone.getText().toString());

                        if(OneSignal.getDeviceState()==null){
                            params.put("deviceId", "");

                        }
                        else if(OneSignal.getDeviceState().getUserId()==null){
                            params.put("deviceId", "");
                        }
                        else{
                            params.put("deviceId", OneSignal.getDeviceState().getUserId());

                        }

                        return params;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue queue = Volley.newRequestQueue(SignupCustomer.this);
                queue.add(request);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupCustomer.this, LoginCustomer.class);
                startActivity(intent);
            }
        });
    }

    public void insertCustomerToSqlite(){
        int myid=mPref.getInt("id", 0);


        MyDBHelper helper = new MyDBHelper(SignupCustomer.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyProject.MyCustomer._ID, myid);
        cv.put(MyProject.MyCustomer._NAME, name.getText()+"");
        cv.put(MyProject.MyCustomer._EMAIL, email.getText()+"");
        cv.put(MyProject.MyCustomer._PHONE, phone.getText()+"");

        db.insert(MyProject.MyCustomer.TABLE_NAME, null, cv);

        helper.close();
    }
}