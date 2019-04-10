package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Helper_SignUpActivity extends AppCompatActivity {

    EditText  email, password;
    Button submit;
    String  emailHolder, passwordHolder;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    Boolean editTextStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper__sign_up);

        email = (EditText) findViewById(R.id.txt1);
        password = (EditText) findViewById(R.id.txt2);
        submit = (Button) findViewById(R.id.btn2);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Helper_SignUpActivity.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailHolder = email.getText().toString().trim();
                passwordHolder = password.getText().toString().trim();

                CheckEditTextIsEmptyOrNot();
                if (editTextStatus) {
                    if ((passwordHolder.length() >= 8)) {
                                UserRegistrationFunction();
                                saveInDatabase();
                    }else {
                        Toast.makeText(Helper_SignUpActivity.this, "Please Password must be more than or equal 8 letters or numbers", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Helper_SignUpActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                } }
        });
    }

    public void UserRegistrationFunction() {
        progressDialog.setMessage("Please Wait, We are Registering Your Data");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailHolder, passwordHolder).
                addOnCompleteListener(Helper_SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Helper_SignUpActivity.this, "User Registration Successfully", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            Intent intent = new Intent(Helper_SignUpActivity.this, RequestsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Helper_SignUpActivity.this, "Something Went Wrong Or you entered invalid data.", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void CheckEditTextIsEmptyOrNot() {
        if (TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(passwordHolder)) {
            editTextStatus = false;
        } else {
            editTextStatus = true;
        }

    }

    public void saveInDatabase() {
        RequestQueue queue = Volley.newRequestQueue(Helper_SignUpActivity.this);
        String url = "http://192.168.1.3:8383/registerHelper";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("true")) {
                            finish();
                            Intent intent = new Intent(Helper_SignUpActivity.this, RequestsActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.err.println("error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", ((EditText) findViewById(R.id.txt1)).getText().toString());
                params.put("password", ((EditText) findViewById(R.id.txt2)).getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


}
