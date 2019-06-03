package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private CheckBox checkBox;
    private String emailHolder, passwordHolder;
    private Button Login, SignUP;
    private Boolean editTextStatus;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RequestActivity.class);
                startActivity(intent);
            }
        });

        email = (EditText) findViewById(R.id.txt1);
        password = (EditText) findViewById(R.id.txt2);

        Login = (Button) findViewById(R.id.btn1);
        SignUP = (Button) findViewById(R.id.btn2);
        checkBox=(CheckBox) findViewById(R.id.ch);

        progressDialog = new ProgressDialog(LoginActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();
                    if (editTextStatus) {
                        if (passwordHolder.length() >= 8) {
                            LoginFunction();
                            loginInDatabase();
                        }else {
                            Toast.makeText(LoginActivity.this, "Please Password must be more than or equal 8 letters or numbers", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Please Fill All the Fields", Toast.LENGTH_LONG).show();
                    }
                }
        });

        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this, UserTypeActivity.class);
                startActivity(intent);

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });
    }

    public void CheckEditTextIsEmptyOrNot() {
        emailHolder= email.getText().toString().trim();
        passwordHolder = password.getText().toString().trim();
        if (TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(passwordHolder)) {
            editTextStatus = false;
        } else {
            editTextStatus = true;
        }
    }

    public void LoginFunction() {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailHolder, passwordHolder)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            finish();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Email or Password Not found, Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void loginInDatabase() {
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = "http://192.168.1.3:8383/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            finish();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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
                params.put("email", ((EditText) findViewById(R.id.txt3)).getText().toString());
                params.put("password", ((EditText) findViewById(R.id.txt4)).getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}

