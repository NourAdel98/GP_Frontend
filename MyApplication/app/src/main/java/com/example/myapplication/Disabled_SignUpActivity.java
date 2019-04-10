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

public class Disabled_SignUpActivity extends AppCompatActivity {

    private EditText fName, lName, email, password, confirmPass, disabilityType, address, age, phone;
    private Button SignUp;
    private String fNameHolder, lNameHolder, emailHolder, passwordHolder, ConfirmPassswordHolder, disabilityTypeHolder, addressHolder, ageHolder, phoneHolder;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Boolean editTextStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled_sign_up);
        fName = (EditText) findViewById(R.id.txt1);
        lName = (EditText) findViewById(R.id.txt2);
        email = (EditText) findViewById(R.id.txt3);
        password = (EditText) findViewById(R.id.txt4);
        confirmPass = (EditText) findViewById(R.id.txt5);
        disabilityType = (EditText) findViewById(R.id.txt6);
        address = (EditText) findViewById(R.id.txt7);
        age = (EditText) findViewById(R.id.txt8);
        phone = (EditText) findViewById(R.id.txt9);
        SignUp = (Button) findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Disabled_SignUpActivity.this);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fNameHolder = fName.getText().toString().trim();
                lNameHolder = lName.getText().toString().trim();
                emailHolder = email.getText().toString().trim();
                passwordHolder = password.getText().toString().trim();
                ConfirmPassswordHolder = confirmPass.getText().toString().trim();
                disabilityTypeHolder = disabilityType.getText().toString().trim();
                addressHolder = address.getText().toString().trim();
                ageHolder = age.getText().toString().trim();
                phoneHolder = phone.getText().toString().trim();

                CheckEditTextIsEmptyOrNot();
                if (editTextStatus) {
                    if ((ConfirmPassswordHolder.equals(passwordHolder))&&(passwordHolder.length() >= 8 && ConfirmPassswordHolder.length() >= 8)) {
                            if (Integer.parseInt(ageHolder) > 18) {
                                UserRegistrationFunction();
                                saveInDatabase();
                            } else {
                                Toast.makeText(Disabled_SignUpActivity.this, "Please enter supporter phone number.", Toast.LENGTH_LONG).show();
                                ((EditText) findViewById(R.id.txt1)).setText(fNameHolder);
                                ((EditText) findViewById(R.id.txt2)).setText(lNameHolder);
                                ((EditText) findViewById(R.id.txt3)).setText(emailHolder);
                                ((EditText) findViewById(R.id.txt4)).setText(passwordHolder);
                                ((EditText) findViewById(R.id.txt5)).setText(ConfirmPassswordHolder);
                                ((EditText) findViewById(R.id.txt6)).setText(disabilityTypeHolder);
                                ((EditText) findViewById(R.id.txt7)).setText(addressHolder);
                                ((EditText) findViewById(R.id.txt8)).setText(ageHolder);
                                ((EditText) findViewById(R.id.txt9)).setText("");
                                SignUp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserRegistrationFunction();
                                        saveInDatabase();
                                    }
                                });

                            }
                            //"Please Password must be more than or equal 8 letters or numbers"
                           //"Password Confirmation doesn't match Password! Please enter them again."
                    }else {
                        Toast.makeText(Disabled_SignUpActivity.this, "Wrong password! Please enter password again.", Toast.LENGTH_LONG).show();
                        ((EditText) findViewById(R.id.txt1)).setText(fNameHolder);
                        ((EditText) findViewById(R.id.txt2)).setText(lNameHolder);
                        ((EditText) findViewById(R.id.txt3)).setText(emailHolder);
                        ((EditText) findViewById(R.id.txt4)).setText("");
                        ((EditText) findViewById(R.id.txt5)).setText("");
                        ((EditText) findViewById(R.id.txt6)).setText(disabilityTypeHolder);
                        ((EditText) findViewById(R.id.txt7)).setText(addressHolder);
                        ((EditText) findViewById(R.id.txt8)).setText(ageHolder);
                        ((EditText) findViewById(R.id.txt9)).setText(phoneHolder);
                    }
                } else {
                    Toast.makeText(Disabled_SignUpActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void UserRegistrationFunction() {
        progressDialog.setMessage("Please Wait, We are Registering Your Data on Server");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailHolder, passwordHolder).
                addOnCompleteListener(Disabled_SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Disabled_SignUpActivity.this, "User Registration Successfully", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            Intent intent = new Intent(Disabled_SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Disabled_SignUpActivity.this, "Something Went Wrong Or your entered invalid data.", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void CheckEditTextIsEmptyOrNot() {
        if (TextUtils.isEmpty(fNameHolder)||TextUtils.isEmpty(lNameHolder)||TextUtils.isEmpty(emailHolder) ||
                TextUtils.isEmpty(passwordHolder)||TextUtils.isEmpty(ConfirmPassswordHolder)||TextUtils.isEmpty(disabilityTypeHolder)||
                TextUtils.isEmpty(addressHolder)||TextUtils.isEmpty(ageHolder)) {
            editTextStatus = false;
        } else {
            editTextStatus = true;
        }

    }

    public void saveInDatabase() {
        RequestQueue queue = Volley.newRequestQueue(Disabled_SignUpActivity.this);
        String url = "http://192.168.1.3:8383/registerDisabled";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            finish();
                            Intent intent = new Intent(Disabled_SignUpActivity.this, HomeActivity.class);
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
                params.put("age", ((EditText) findViewById(R.id.txt8)).getText().toString());
                params.put("fname", ((EditText) findViewById(R.id.txt1)).getText().toString());
                params.put("lname", ((EditText) findViewById(R.id.txt2)).getText().toString());
                params.put("password", ((EditText) findViewById(R.id.txt4)).getText().toString());
                params.put("address", ((EditText) findViewById(R.id.txt7)).getText().toString());
                params.put("disabilityType", ((EditText) findViewById(R.id.txt6)).getText().toString());
                params.put("phoneNumber", ((EditText) findViewById(R.id.txt9)).getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);
    }


}

