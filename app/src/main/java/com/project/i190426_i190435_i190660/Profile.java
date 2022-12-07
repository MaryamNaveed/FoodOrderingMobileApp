package com.project.i190426_i190435_i190660;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    ImageView back;
    SharedPreferences mPref;
    TextView name, email, phone;
    ImageButton editname, editemail, editphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);

        back=findViewById(R.id.back);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);

        editname=findViewById(R.id.editname);
        editemail=findViewById(R.id.editemail);
        editphone=findViewById(R.id.editphone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(Profile.this);
                final View view1 = factory.inflate(R.layout.pop_up_edit, null);
                EditText editText = view1.findViewById(R.id.editText);
                editText.setText(name.getText().toString());
                Button edit= view1.findViewById(R.id.edit);
                Button cancel= view1.findViewById(R.id.cancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this).setView(view1);

                AlertDialog dialog=builder.create();

                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/editName.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject res1 = new JSONObject(response);

                                            if(res1.getInt("reqcode")==1){
                                                Toast.makeText(Profile.this, "Updated Name", Toast.LENGTH_LONG).show();
                                                name.setText(editText.getText().toString());
                                                dialog.dismiss();

                                            }
                                            else {
                                                Toast.makeText(Profile.this, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(Profile.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
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
                                    params.put("name",editText.getText().toString());





                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(Profile.this);
                        queue.add(request);

                    }
                });

            }
        });

        editemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(Profile.this);
                final View view1 = factory.inflate(R.layout.pop_up_edit, null);
                EditText editText = view1.findViewById(R.id.editText);
                editText.setText(email.getText().toString());
                Button edit= view1.findViewById(R.id.edit);
                Button cancel= view1.findViewById(R.id.cancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this).setView(view1);

                AlertDialog dialog=builder.create();

                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/editEmail.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject res1 = new JSONObject(response);

                                            if(res1.getInt("reqcode")==1){
                                                Toast.makeText(Profile.this, "Updated Email", Toast.LENGTH_LONG).show();
                                                email.setText(editText.getText().toString());
                                                dialog.dismiss();

                                            }
                                            else {
                                                Toast.makeText(Profile.this, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(Profile.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
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
                                params.put("email",editText.getText().toString());





                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(Profile.this);
                        queue.add(request);

                    }
                });


            }
        });

        editphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(Profile.this);
                final View view1 = factory.inflate(R.layout.pop_up_edit, null);
                EditText editText = view1.findViewById(R.id.editText);
                editText.setText(phone.getText().toString());
                Button edit= view1.findViewById(R.id.edit);
                Button cancel= view1.findViewById(R.id.cancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this).setView(view1);

                AlertDialog dialog=builder.create();

                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        StringRequest request = new StringRequest(Request.Method.POST, Ip.ipAdd + "/editPhone.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject res1 = new JSONObject(response);

                                            if(res1.getInt("reqcode")==1){
                                                Toast.makeText(Profile.this, "Updated Phone", Toast.LENGTH_LONG).show();
                                                phone.setText(editText.getText().toString());
                                                dialog.dismiss();


                                            }
                                            else {
                                                Toast.makeText(Profile.this, res1.get("reqmsg").toString(), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(Profile.this, "Cannot Parse JSON", Toast.LENGTH_LONG).show();
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
                                params.put("phone",editText.getText().toString());





                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(Profile.this);
                        queue.add(request);

                    }
                });



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