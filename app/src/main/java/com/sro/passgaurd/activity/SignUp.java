package com.sro.passgaurd.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sro.passgaurd.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    String url = "https://myprojectapis.herokuapp.com/users";
    TextInputEditText name, email, password;
    String names, passwords, emails;
    Button btn;
    TextView logintxt;
    SharedPreferences preferences;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn = findViewById(R.id.register);
        logintxt = findViewById(R.id.login);
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                names = name.getText().toString();
                emails = email.getText().toString();
                passwords = password.getText().toString();
                register(names, emails, passwords);
            }
        });

    }


    private void register(String names, String emails, String passwords) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences preferences = getSharedPreferences("pin", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pincode", "");
                editor.apply();
                Toast.makeText(SignUp.this, "Registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, Login.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", names);
                params.put("email", emails);
                params.put("password", passwords);
                return params;
            }
        };

        queue.add(stringRequest);
    }


    public void getAllUsers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray valarray = new JSONArray(response);

                    for (int i = 0; i < valarray.length(); i++) {
                        String str;
                        str = valarray.getJSONObject(i).getString("email");
                        Log.d("hello", str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hello", error.toString());
            }
        });

        queue.add(request);
    }
}