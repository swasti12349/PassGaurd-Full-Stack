package com.sro.passgaurd.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sro.passgaurd.R;
import com.sro.passgaurd.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;

public class Login extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    String url = "https://myprojectapis.herokuapp.com/users";
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String str = preferences.getString("remember", "");

        SharedPreferences pref = getSharedPreferences("pin", MODE_PRIVATE);
        String str1 = pref.getString("pincode", "");

        if (str.equals("true") && str1!="") {
                AlertDialog dialog;
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login.this);
                View mView = getLayoutInflater().inflate(R.layout.enterpassword, null);
                mBuilder.setTitle("Password").setView(mView).setCancelable(false);

                final EditText qw = (EditText) mView.findViewById(R.id.enterpassword);


                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog = mBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validatePassword(qw.getText().toString());
                    }

                });
            }

        loginBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();


                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });

        loginBinding.reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        loginBinding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                getAllUsers(loginBinding.email.getText().toString(), loginBinding.password.getText().toString());
            }
        });
    }

    private void validatePassword(String toString) {

        SharedPreferences preferences = getSharedPreferences("pin", MODE_PRIVATE);
        String str = preferences.getString("pincode", "");

        if (toString.equals(str)){
            startActivity(new Intent(Login.this, Home.class));
        }else{
            Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }

    }

    public void getAllUsers(String emails, String passwords) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray valarray = new JSONArray(response);

                    for (int i = 0; i < valarray.length(); i++) {
                        String email, pass;
                        email = valarray.getJSONObject(i).getString("email");
                        pass = valarray.getJSONObject(i).getString("password");

                        if (emails.equals(email) && pass.equals(passwords)) {

                            SharedPreferences preferences = getSharedPreferences("userd", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", emails);
                            editor.apply();

//                            Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
//                            Intent in = new Intent(getApplicationContext(), Home.class);
//                            in.putExtra("emailsd", emails);
//                            startActivity(in);

                            createPIN(emails);

                        } else if (emails.equals(email) && !pass.equals(passwords)) {
                            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }

                    progressDialog.dismiss();

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

    public void createPIN(String emails) {
        AlertDialog dialog;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login.this);
        View mView = getLayoutInflater().inflate(R.layout.createpassword, null);
        mBuilder.setTitle("Create Password").setView(mView).setCancelable(false);

        final EditText np = (EditText) mView.findViewById(R.id.newpassword);
        final EditText cp = (EditText) mView.findViewById(R.id.confirmpassword);


        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        dialog = mBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(np.getText().toString(), cp.getText().toString(), emails, cp);
            }

        });
    }

    private void fun(String np, String cp, String emails, EditText cpe) {

        if (np.equals(cp)){
            SharedPreferences preferences = getSharedPreferences("pin", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("pincode", np);
            editor.apply();
            Intent in = new Intent(getApplicationContext(), Home.class);
            in.putExtra("emailsd", emails);
            startActivity(in);
            finish();
        }else{
            cpe.setError("Password must be equal");
        }
    }
}