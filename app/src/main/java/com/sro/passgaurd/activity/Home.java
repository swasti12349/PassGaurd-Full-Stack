package com.sro.passgaurd.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.sro.passgaurd.adapter.dataAdapter;
import com.sro.passgaurd.databinding.ActivityHomeBinding;
import com.sro.passgaurd.model.dataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    ActivityHomeBinding homeBinding;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<dataModel> dataModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        recyclerView = findViewById(R.id.dataRV);
        dataModelArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeBinding.progressBar.setVisibility(View.GONE);
            }
        }, 4000);

        SharedPreferences preferences = getSharedPreferences("userd", MODE_PRIVATE);
        String str = preferences.getString("email", "");

        getData(str);

        homeBinding.addid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Home.this, Create.class));
            }
        });


    }

    void getData(String email) {
        String url = "https://myprojectapis.herokuapp.com/usersdata";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        String title = array.getJSONObject(i).getString("title");
                        String password = array.getJSONObject(i).getString("password");
                        dataModel dataModel = new dataModel(title, password);
                        dataModelArrayList.add(dataModel);
                    }

                    adapter = new dataAdapter(Home.this, dataModelArrayList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put("email", email);
                return m;
            }
        };

        queue.add(request);
    }

}