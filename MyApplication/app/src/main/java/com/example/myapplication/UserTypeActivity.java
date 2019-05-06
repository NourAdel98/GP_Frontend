package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class UserTypeActivity extends AppCompatActivity {
    private CheckBox checkBox1,checkBox2,checkBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);

        checkBox1 =(CheckBox) findViewById(R.id.ch1);
        checkBox2= (CheckBox) findViewById(R.id.ch2);
        checkBox3= (CheckBox) findViewById(R.id.ch3);


        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserTypeActivity.this, Disabled_SignUpActivity.class);
                startActivity(intent);

            }
        });


        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserTypeActivity.this, Supporter_SignUpActivity.class);
                startActivity(intent);
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserTypeActivity.this, Helper_SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}