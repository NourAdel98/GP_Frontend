package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPasswordActivity extends AppCompatActivity {
    private String email,password;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        System.out.println("email in pass: "+email);

        ok=(Button)findViewById(R.id.btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(GetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void jasonParse() {
        String url = "http://192.168.1.3:8383/sendPassword";
        RequestQueue queue = Volley.newRequestQueue(GetPasswordActivity.this);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("password");
                            password = jsonObject.getString("password");
                            ((EditText) findViewById(R.id.txt2)).setText(password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(object);
    }
}
