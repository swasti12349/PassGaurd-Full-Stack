package com.sro.passgaurd.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sro.passgaurd.R;
import com.sro.passgaurd.databinding.ActivityCreateBinding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Create extends AppCompatActivity {
    ActivityCreateBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(Create.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        binding.addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.title.getText().toString();
                String password = binding.password.getText().toString();
                SharedPreferences preferences = getSharedPreferences("userd", MODE_PRIVATE);
                String str = preferences.getString("email", "");
                uploadData(title, password, str);
            }
        });


    }

    private void uploadData(String title, String password, String email) {
        String url = "https://myprojectapis.herokuapp.com/userdata";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Create.this, response, Toast.LENGTH_SHORT).show();
                Log.d("polk", response);
                startActivity(new Intent(Create.this, Home.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("polk", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("title", title);
                map.put("password", password);
                map.put("email", email);
                return map;
            }
        };
        queue.add(request);
    }
}