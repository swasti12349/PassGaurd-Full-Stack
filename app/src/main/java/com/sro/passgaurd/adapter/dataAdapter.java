package com.sro.passgaurd.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sro.passgaurd.R;
import com.sro.passgaurd.activity.Login;
import com.sro.passgaurd.activity.UpdateActivity;
import com.sro.passgaurd.model.dataModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class dataAdapter extends RecyclerView.Adapter<dataAdapter.dataViewHolder> {
    Context context;
    ArrayList<dataModel> dataModelArrayList;

    public dataAdapter(Context context, ArrayList<dataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public dataAdapter.dataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dataitem, parent, false);
        return new dataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull dataAdapter.dataViewHolder holder, int position) {
        dataModel m = dataModelArrayList.get(position);
        int p = position;
        holder.title.setText(m.getTitle());
        holder.password.setText(m.getPassword());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateActivity.class);
                i.putExtra("title", m.getTitle());
                i.putExtra("password", m.getPassword());
                context.startActivity(i);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(m);
                dataModelArrayList.remove(m);
                notifyItemRemoved(p);
                notifyDataSetChanged();
            }
        });


    }

    private void deleteData(dataModel model) {
        String url = "https://myprojectapis.herokuapp.com/deletedata";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hello", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                SharedPreferences preferences = context.getSharedPreferences("userd", Context.MODE_PRIVATE);
                String email = preferences.getString("email", "");
                m.put("email", email);
                m.put("title", model.getTitle());
                return m;
            }
        };

        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class dataViewHolder extends RecyclerView.ViewHolder {
        TextView title, password;
        ImageView delete;
        public dataViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            password = itemView.findViewById(R.id.password);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
