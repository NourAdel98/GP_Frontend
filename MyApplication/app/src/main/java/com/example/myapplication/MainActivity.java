package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onClick();
    }

    void onClick(){

        Button btn = (Button) findViewById(R.id.btn);
        final TextView textView = (TextView) findViewById(R.id.txt1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="http://192.168.43.198:8383/register";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.contains("true")){
                                    Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
                                    startActivity(intent);
                                }else{
                                    ((TextView) findViewById(R.id.txt1)).setText("try again :(");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("errorrrrrrrrrr: " + error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("username",((TextView) findViewById(R.id.txt1)).getText().toString());
                        params.put("pass",((TextView) findViewById(R.id.txt2)).getText().toString());

                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

}
