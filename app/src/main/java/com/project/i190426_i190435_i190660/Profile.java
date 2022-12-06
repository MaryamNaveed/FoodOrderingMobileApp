package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    ImageView back;
    SharedPreferences mPref;
    TextView name, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);

        back=findViewById(R.id.back);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        StringRequest request1 = new StringRequest(Request.Method.POST,
                Ip.ipAdd + "/getCustomerbyId.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject res = new JSONObject(response);

                            if (res.getInt("reqcode") == 1) {
                                JSONObject user=res.getJSONObject("user");

                                name.setText(user.getString("name"));
                                email.setText(user.getString("email"));
                                phone.setText(user.getString("phone"));




                            }
                            else {
                                Toast.makeText(Profile.this, res.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (Exception e){

                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Profile.this, "Connection Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("id", String.valueOf(mPref.getInt("id", 0)));

                return params;
            }
        };

        RequestQueue queue1 = Volley.newRequestQueue(Profile.this);
        queue1.add(request1);
    }
}