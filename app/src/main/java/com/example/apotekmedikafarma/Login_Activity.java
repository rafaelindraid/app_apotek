package com.example.apotekmedikafarma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {
    EditText username, password;
    LinearLayout btnlogin;
    private ProgressBar loading;
    ProgressDialog progressDialog;
    private static String URL_LOGIN = "http://10.0.2.2:8080/medika/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.edtlogin_username);
        password = findViewById(R.id.edtlogin_password);
        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername = username.getText().toString().trim();
                String mPassword = password.getText().toString().trim();
                if (!mUsername.isEmpty() || !mPassword.isEmpty()) {
                    CheckLogin(mUsername, mPassword);
                } else {
                    username.setError("Masukkan Username");
                    password.setError("Masukkan Password");
                }

            }
        });
    }

    public void CheckLogin(final String mUsername, final String mPassword){
        if(checkNetworkConnection()){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if(resp.equals("[{\"status\":\"OK\"}]")){
                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent HomeIntent = new Intent(Login_Activity.this, MainActivity.class);
                                    startActivity(HomeIntent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Periksa Username dan Password", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", mUsername);
                    params.put("password", mPassword);
                    return  params;
                }
            };
            VolleyConnection.getInstance(Login_Activity.this).addToRequestQueue(stringRequest);


        }else{
            Toast.makeText(getApplicationContext(),"tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }


    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());


    }
}


