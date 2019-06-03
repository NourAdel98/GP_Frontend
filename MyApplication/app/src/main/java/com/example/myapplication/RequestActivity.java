package com.example.myapplication;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Pickers.DatePickerFragment;
import com.example.myapplication.Pickers.TimePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    private RequestQueue queue;
    private TextView editDate;
    private TextView editTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        queue = Volley.newRequestQueue(RequestActivity.this);

        editDate = findViewById(R.id.editDate);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        editDate.setText(format.format(Calendar.getInstance().getTime()));

        editTime = findViewById(R.id.editTime);
        format = new SimpleDateFormat("HH:mm");
        editTime.setText(format.format(new Date()));

        setDateAndTime();
        setSpinner();
        makeRequest();
    }

    private void getAllServices(final IVolleyResponseCallback callback) {

        String url = "http://192.168.43.198:8383/get-all-services";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray services = new JSONArray(response);
                    callback.onSuccess(services);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);
            }
        });
        queue.add(request);
    }

    private void setSpinner() {

        getAllServices(new IVolleyResponseCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray res) {

                String[] services = new String[res.length()];
                for (int i = 0; i < res.length(); i++) {

                    try {

                        services[i] = (String) res.getJSONObject(i).get("description");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Spinner spinner = (Spinner) findViewById(R.id.request);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RequestActivity.this,
                        android.R.layout.simple_spinner_item, services);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }

    private void setDateAndTime() {

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), "Set Date");
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getSupportFragmentManager(), "Set Time");
            }
        });
    }

    private void makeRequest() {

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String service = ((Spinner) findViewById(R.id.request)).getSelectedItem().toString();
                String date = editDate.getText().toString();
                String time = editTime.getText().toString();

                doRequest(service,date,time);
            }
        });
    }

    private void doRequest(final String service, final String date, final String time){

        String url = "http://192.168.43.198:8383/make-request";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {
                    Toast.makeText(RequestActivity.this, "done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RequestActivity.this, "something wrong, try again!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                Date dateObj = new Date();
                String [] dateDetails = date.split("/");
                String [] timeDetails = time.split(":");

                // set day
                dateObj.setDate(Integer.parseInt(dateDetails[0]));

                // month between 0-11 so MAY is in 4th index
                dateObj.setMonth(Integer.parseInt(dateDetails[1]) - 1);

                // year after 1900 so need to minus 1900 from the value that I entered
                dateObj.setYear(Integer.parseInt(dateDetails[2]) - 1900);

                // to set time
                dateObj.setHours(Integer.parseInt(timeDetails[0]));
                dateObj.setMinutes(Integer.parseInt(timeDetails[1]));

                params.put("serviceId", service);
                params.put("date", dateObj.toString());
                params.put("payMethod", "cash");
                params.put("requestState", "false");

                return params;
            }
        };
        queue.add(request);
    }
}
