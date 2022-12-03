package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

public class LoginAdmin extends AppCompatActivity {

    TextView show;
    EditText email, password;
    Button signin;
    boolean showed=false;
    SharedPreferences mPref;
    SharedPreferences.Editor editmPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        show=findViewById(R.id.show);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signin=findViewById(R.id.signin);

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

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/loginAdmin.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                System.out.println(response);


                                try {
                                    JSONObject res = new JSONObject(response);

                                    if (res.getInt("reqcode") == 1) {

                                        int idUser=res.getInt("id");

                                        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/updateAdminDeviceId.php",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {
                                                            JSONObject res1 = new JSONObject(response);

                                                            if(res1.getInt("reqcode")==1){
                                                                Toast.makeText(LoginAdmin.this, "Signed In", Toast.LENGTH_LONG).show();
                                                                editmPref.putBoolean("loggedInAdmin", true);
                                                                editmPref.putInt("id", idUser);
                                                                editmPref.apply();
                                                                editmPref.commit();
                                                                Intent intent=new Intent(LoginAdmin.this, MainPageRestaurant.class);
                                                                startActivity(intent);
                                                            }
                                                            else {
                                                                Toast.makeText(LoginAdmin.this, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(LoginAdmin.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                                                        }



                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(LoginAdmin.this, "Connection Error", Toast.LENGTH_LONG).show();
                                                        Toast.makeText(LoginAdmin.this, error.toString(), Toast.LENGTH_LONG).show();

                                                    }
                                                }){
                                            @Nullable
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();

                                                if(OneSignal.getDeviceState()==null){
                                                    params.put("id", String.valueOf(idUser));
                                                    params.put("deviceId", "");

                                                }
                                                else if(OneSignal.getDeviceState().getUserId()==null){
                                                    params.put("id", String.valueOf(idUser));
                                                    params.put("deviceId", "");
                                                }
                                                else{
                                                    params.put("id", String.valueOf(idUser));
                                                    params.put("deviceId", OneSignal.getDeviceState().getUserId());

                                                }



                                                return params;
                                            }
                                        };

                                        RequestQueue queue = Volley.newRequestQueue(LoginAdmin.this);
                                        queue.add(request);


                                    } else {
                                        Toast.makeText(LoginAdmin.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginAdmin.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
                                    Toast.makeText(LoginAdmin.this, e.toString(), Toast.LENGTH_LONG).show();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginAdmin.this, "Connection Error", Toast.LENGTH_LONG).show();
                                Toast.makeText(LoginAdmin.this, error.toString(), Toast.LENGTH_LONG).show();

                            }


                        }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());

                        return params;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue queue = Volley.newRequestQueue(LoginAdmin.this);
                queue.add(request);

//                Intent intent=new Intent(LoginAdmin.this, MainPageRestaurant.class);
//                startActivity(intent);
            }
        });
    }
}