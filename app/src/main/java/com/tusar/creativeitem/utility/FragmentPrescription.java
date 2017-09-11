package com.tusar.creativeitem.utility;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tusar.creativeitem.PatientActivity;
import com.tusar.creativeitem.PatientDetailsActivity;
import com.tusar.creativeitem.PrescriptionDetailsActivity;
import com.tusar.creativeitem.R;
import com.tusar.creativeitem.helper.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FragmentPrescription extends Fragment {

    View view;
    private DatabaseHandler db;
    private String patient_id;
    ProgressDialog pDialog;
    private TextView tvPrescription;
    private ListView list;
    ArrayList<String> date_array = new ArrayList<String>();
    ArrayList<String> prescription_id_array = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseHandler(getActivity());
        pDialog = new ProgressDialog(getActivity());
        view = inflater.inflate(R.layout.fragment_prescription, container, false);

        patient_id= getArguments().getString("patient_id");
        getPrescription(patient_id);
        list=(ListView) view.findViewById(R.id.listPrescription);
        tvPrescription=(TextView) view.findViewById(R.id.tvPrescription);
        return  view;
    }

    public void getPrescription(final String patient_id){
        pDialog.setMessage("Loading...");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = AppConfigURL.URL + "get_prescription_history_of_patient";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //split to string from json response
                        System.out.println("Response: "+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                long timestamp     = Long.parseLong(jsonObject.getString("timestamp"));
                                String prescription_id = jsonObject.getString("prescription_id");
                                String date = getDate(timestamp*1000);
                                date_array.add(date);
                                prescription_id_array.add(prescription_id);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (date_array.size()== 0){
                            tvPrescription.setVisibility(View.VISIBLE);
                            hideDialog();
                        }
                        else{
                            CustomPrescriptionList adapter = new
                                    CustomPrescriptionList(getActivity(), date_array);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Intent i = new Intent(getActivity(), PrescriptionDetailsActivity.class);
                                    i.putExtra("prescription_id", prescription_id_array.get(position));
                                    startActivity(i);
                                }
                            });
                            hideDialog();
                        }

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

                Map<String, String> params = new HashMap<>();
                params.put("auth_token", token);
                params.put("user_id", user_id);
                params.put("patient_id", patient_id);
                params.put("authenticate", "true");

                return params;

            }
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
    private String getDate(long time) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
