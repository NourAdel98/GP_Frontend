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

public class Supporter_SignUpActivity extends AppCompatActivity {


    EditText fName, lName, email, password, confirmPass, address, phone;
    Button SignUp;
    String fNameHolder, lNameHolder, emailHolder, passwordHolder, ConfirmPassswordHolder, addressHolder, phoneHolder;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    Boolean editTextStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter__sign_up);
        fName = (EditText) findViewById(R.id.txt1);
        lName = (EditText) findViewById(R.id.txt2);
        email = (EditText) findViewById(R.id.txt3);
        password = (EditText) findViewById(R.id.txt4);
        confirmPass = (EditText) findViewById(R.id.txt5);
        address = (EditText) findViewById(R.id.txt6);
        phone = (EditText) findViewById(R.id.txt7);

        SignUp = (Button) findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Supporter_SignUpActivity.this);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fNameHolder = fName.getText().toString().trim();
                lNameHolder = lName.getText().toString().trim();
                emailHolder = email.getText().toString().trim();
                passwordHolder = password.getText().toString().trim();
                ConfirmPassswordHolder = confirmPass.getText().toString().trim();
                addressHolder = address.getText().toString().trim();
                phoneHolder = phone.getText().toString().trim();

                CheckEditTextIsEmptyOrNot();
                if (editTextStatus) {
                    if ((ConfirmPassswordHolder.equals(passwordHolder))&&(passwordHolder.length() >= 8 && ConfirmPassswordHolder.length() >= 8)) {
                        UserRegistrationFunction();
                            saveInDatabase();
                    } else {
                        Toast.makeText(Supporter_SignUpActivity.this, "Wrong password! Please enter password again.", Toast.LENGTH_LONG).show();
                        ((EditText) findViewById(R.id.txt1)).setText(fNameHolder);
                        ((EditText) findViewById(R.id.txt2)).setText(lNameHolder);
                        ((EditText) findViewById(R.id.txt3)).setText(emailHolder);
                        ((EditText) findViewById(R.id.txt4)).setText("");
                        ((EditText) findViewById(R.id.txt5)).setText("");
                        ((EditText) findViewById(R.id.txt6)).setText(addressHolder);
                        ((EditText) findViewById(R.id.txt7)).setText(phoneHolder);
                    }
                } else {
                    Toast.makeText(Supporter_SignUpActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void UserRegistrationFunction() {
        progressDialog.setMessage("Please Wait, We are Registering Your Data on Server");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailHolder, passwordHolder).
                addOnCompleteListener(Supporter_SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Supporter_SignUpActivity.this, "User Registration Successfully", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            Intent intent = new Intent(Supporter_SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Supporter_SignUpActivity.this, "Something Went Wrong Or your entered invalid data.", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void CheckEditTextIsEmptyOrNot() {
        if (TextUtils.isEmpty(fNameHolder)||TextUtils.isEmpty(lNameHolder)||TextUtils.isEmpty(emailHolder) ||
                TextUtils.isEmpty(passwordHolder)||TextUtils.isEmpty(ConfirmPassswordHolder)|| TextUtils.isEmpty(addressHolder)||
                TextUtils.isEmpty(phoneHolder)) {
            editTextStatus = false;
        } else {
            editTextStatus = true;
        }

    }

    public void saveInDatabase() {
        RequestQueue queue = Volley.newRequestQueue(Supporter_SignUpActivity.this);
        String url = "http://192.168.1.3:8383/registerSupporter";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("true")) {
                            finish();
                            Intent intent = new Intent(Supporter_SignUpActivity.this, HomeActivity.class);
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
                params.put("fname", ((EditText) findViewById(R.id.txt1)).getText().toString());
                params.put("lname", ((EditText) findViewById(R.id.txt2)).getText().toString());
                params.put("password", ((EditText) findViewById(R.id.txt4)).getText().toString());
                params.put("address", ((EditText) findViewById(R.id.txt6)).getText().toString());
                params.put("phoneNumber", ((EditText) findViewById(R.id.txt7)).getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);
    }
}
