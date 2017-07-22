package com.tusar.creativeitem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.tusar.creativeitem.helper.DatabaseHandler;
import com.tusar.creativeitem.utility.AppConfigURL;
import com.tusar.creativeitem.utility.CustomAppointmentList;
import com.tusar.creativeitem.utility.CustomBillingList;
import com.tusar.creativeitem.utility.CustomBillingList1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BillingActivity extends BaseActivity{

    private com.github.clans.fab.FloatingActionMenu menuRed;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    private com.github.clans.fab.FloatingActionButton fab3;
    private com.github.clans.fab.FloatingActionButton fab4;

    private DatabaseHandler db;
    private ListView list;
    private TextView tvdatePicker;
    ImageButton btnDecrement,btnIncrement;
    ArrayList<String> name_array = new ArrayList<String>();
    ArrayList<String> charge_array = new ArrayList<String>();
    private TextView tvbilling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_billing, contentFrameLayout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(4).setChecked(true);

        db = new DatabaseHandler(this);

        //Floating nav button
        menuRed = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab4);

        menuRed.setClosedOnTouchOutside(true);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BillingActivity.this, NewAppointment.class);
                startActivity(i);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BillingActivity.this, PrescriptionActivity.class);
                startActivity(i);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BillingActivity.this, NewPatient.class);
                startActivity(i);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BillingActivity.this, NewBilling.class);
                startActivity(i);
            }
        });
        btnDecrement = (ImageButton) findViewById(R.id.img1);



        ArrayList<HashMap<String,String>> chamber_list;
        chamber_list = db.getAllChamber();

        for(int i=0;i<chamber_list.size();i++){
            HashMap<String,String> content = new HashMap<String, String>();
            content = chamber_list.get(i);
            String status = content.get("status");
            if(status.equals("Selected")){
                String chamber_id = content.get("chamber_id");
                getAllBilling(chamber_id);
            }
        }

        tvdatePicker = (TextView) findViewById(R.id.datePicker);
        tvdatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(BillingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth+1;
                        tvdatePicker.setText(selectedday+ "/" + selectedmonth+ "/" + selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String today = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
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

                Calendar cal = Calendar.getInstance();
                cal.setTime ( date ); // convert your date to Calendar object
                int daysToDecrement = -1;
                cal.add(Calendar.DATE, daysToDecrement);
                date = cal.getTime();
                System.out.println("Decrement date : "+date);


                tvdatePicker.setText("Yesterday");
                if(btnDecrement.isClickable()){
                    btnDecrement.setClickable(false);
                }

            }
        });


        list=(ListView)findViewById(R.id.list);
        tvbilling=(TextView)findViewById(R.id.tvbilling);


    }//onCreate ends here

    public void getAllBilling(final String chamber_id){
        RequestQueue queue = Volley.newRequestQueue(BillingActivity.this);
        String url = AppConfigURL.URL + "get_invoices";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //split to string from json response
                        System.out.println(" Billing: "+response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String patient_name     = jsonObject.getString("patient_name");
                                String charge     = jsonObject.getString("charge");

                                name_array.add(patient_name);
                                charge_array.add(charge+ "৳");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (name_array.size()== 0){
                            tvbilling.setVisibility(View.VISIBLE);
                        }
                        else {
                            CustomBillingList1 adapter = new
                                    CustomBillingList1(BillingActivity.this, name_array, charge_array);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error Toast
                Toast.makeText(getApplicationContext(),"Error Response",Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // get data from sqlite
                HashMap<String, String> user = db.getUser();
                final String token = user.get("token");
                final String user_id = user.get("user_id");

                String today = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
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
                params.put("chamber_id", chamber_id);
                // params.put("timestamp", String.valueOf(timeStampDate.getTime()/1000));
                params.put("authenticate", "true");
                System.out.println("params >>> "+params);
                return params;

            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
