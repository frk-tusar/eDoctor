package com.tusar.creativeitem.utility;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tusar.creativeitem.R;
import com.tusar.creativeitem.helper.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentAppointment extends Fragment {
    View view;
    private DatabaseHandler db;
    private String patient_id;
    ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseHandler(getActivity());
        pDialog = new ProgressDialog(getActivity());
        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        patient_id= getArguments().getString("patient_id");

        getAppointment(patient_id);

        return  view;
    }
    public void getAppointment(final String patient_id){
        pDialog.setMessage("Loading...");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = AppConfigURL.URL + "get_appointment_history_of_patient";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //split to string from json response
                        System.out.println("Response: "+response);

                        hideDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error Toast
                hideDialog();
                Toast.makeText(getActivity(),"Error Response",Toast.LENGTH_LONG).show();

            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // get data from sqlite
                HashMap<String, String> user = db.getUser();
                final String token = user.get("token");
                final String user_id = user.get("user_id");

                String today = (android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
                System.out.println("Today >>> "+today);
                java.text.DateFormat formatter ;
                Date date = null;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = (Date)formatter.parse(today);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                System.out.println("Today is " + timeStampDate.getTime());

                Map<String, String> params = new HashMap<>();
                params.put("auth_token", token);
                params.put("user_id", user_id);
                params.put("patient_id", patient_id);
                params.put("authenticate", "true");
                params.put("timestamp", String.valueOf(timeStampDate.getTime()/1000));
                System.out.println("Params > "+params);

                return params;

            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }


        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
