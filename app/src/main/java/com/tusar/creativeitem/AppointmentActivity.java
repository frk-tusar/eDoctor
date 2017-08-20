package com.tusar.creativeitem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.tusar.creativeitem.helper.DatabaseHandler;
import com.tusar.creativeitem.utility.AppConfigURL;
import com.tusar.creativeitem.utility.CustomAppointmentList;
import com.tusar.creativeitem.utility.CustomPatientList;

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


public class AppointmentActivity extends BaseActivity{

    private com.github.clans.fab.FloatingActionMenu menuRed;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    private com.github.clans.fab.FloatingActionButton fab3;
    private com.github.clans.fab.FloatingActionButton fab4;

    private DatabaseHandler db;
    private ListView list;
    private TextView tvdatePicker;
    ImageButton btnDecrement,btnIncrement;
    private long today_timestamp;
    private TextView tvAppoint;
    ProgressDialog pDialog;

    ArrayList<String> name_array = new ArrayList<String>();
    ArrayList<String> phone_array = new ArrayList<String>();
    ArrayList<String> visit_array = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_appointment, contentFrameLayout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        db = new DatabaseHandler(this);
        pDialog = new ProgressDialog(this);
        //Floating nav button
        menuRed = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu_red);
        fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab4);

        tvAppoint=(TextView) findViewById(R.id.tvAppoint);
        menuRed.setClosedOnTouchOutside(true);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppointmentActivity.this, NewAppointment.class);
                startActivity(i);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppointmentActivity.this, PrescriptionActivity.class);
                startActivity(i);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppointmentActivity.this, NewPatient.class);
                startActivity(i);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppointmentActivity.this, NewBilling.class);
                startActivity(i);
            }
        });
        btnDecrement = (ImageButton) findViewById(R.id.img1);

        String today = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
        Date date = null;
        java.text.DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
        today_timestamp = timeStampDate.getTime()/1000;

        ArrayList<HashMap<String,String>> chamber_list;
        chamber_list = db.getAllChamber();

        for(int i=0;i<chamber_list.size();i++){
            HashMap<String,String> content = new HashMap<String, String>();
            content = chamber_list.get(i);
            String status = content.get("status");
            if(status.equals("Selected")){
                String chamber_id = content.get("chamber_id");

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

                        DatePickerDialog mDatePicker=new DatePickerDialog(AppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                                selectedmonth = selectedmonth+1;
                                tvdatePicker.setText(selectedday+ "/" + selectedmonth+ "/" + selectedyear);
                                Date date1 = null;
                                java.text.DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    date1 = (Date)formatter1.parse(tvdatePicker.getText().toString());
                                    System.out.println("2:> "+date1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                java.sql.Timestamp timeStampDate1 = new Timestamp(date1.getTime());
                                today_timestamp = timeStampDate1.getTime()/1000;
                            }
                        },mYear, mMonth, mDay);
                        mDatePicker.show();
                    }
                });

                getAllAppointments(chamber_id,today_timestamp);
            }
        }



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


    }//onCreate ends here

    public void getAllAppointments(final String chamber_id, final long timestamp){
        pDialog.setMessage("Loading...");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(AppointmentActivity.this);
        String url = AppConfigURL.URL + "get_appointments";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //split to string from json response
                        System.out.println("Response All Appointment: "+response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONArray jsonArray1 = jsonObject.getJSONArray("patient");
                                System.out.println(jsonArray1);
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    String name     = jsonObject1.getString("name");
                                    String phone     = jsonObject1.getString("phone");
                                    name_array.add(name);
                                    phone_array.add(phone);
                                }
                                String visit = jsonObject.getString("is_visited");
                                visit_array.add(visit);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (name_array.size()== 0){
                            tvAppoint.setVisibility(View.VISIBLE);
                            hideDialog();
                        }
                        else
                        {
                            //listview
                            CustomAppointmentList adapter = new
                                    CustomAppointmentList(AppointmentActivity.this, name_array, phone_array, visit_array);
                            list=(ListView)findViewById(R.id.list);
                            list.setAdapter(adapter);
                            hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error Toast

                hideDialog();
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

                Map<String, String> params = new HashMap<>();
                params.put("auth_token", token);
                params.put("user_id", user_id);
                params.put("chamber_id", chamber_id);
                params.put("timestamp", String.valueOf(timestamp));
                params.put("authenticate", "true");
                System.out.println("params >>> "+params);
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

}
