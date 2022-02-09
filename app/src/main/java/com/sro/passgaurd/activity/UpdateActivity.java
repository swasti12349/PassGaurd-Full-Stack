package com.sro.passgaurd.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sro.passgaurd.R;
import com.sro.passgaurd.databinding.ActivityUpdateBinding;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    String email, title, password, newTitle;
    SharedPreferences preferences;
    String url = "https://myprojectapis.herokuapp.com/updatedata";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(UpdateActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Saving");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        binding.title.setText(getIntent().getStringExtra("title"));
        binding.password.setText(getIntent().getStringExtra("password"));
        preferences = getSharedPreferences("userd", MODE_PRIVATE);
        email = preferences.getString("email", "");
        title = getIntent().getStringExtra("title");

        binding.addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTitle = binding.title.getText().toString();
                password = binding.password.getText().toString();
                updateData(title, password, email, newTitle);
            }
        });
    }

    void updateData(String title, String password, String email, String newTitle){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateActivity.this, Home.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put("email", email);
                m.put("password", password);
                m.put("title", title);
                m.put("newtitle", newTitle);
                return m;
            }
        };

        queue.add(request);


    }
}