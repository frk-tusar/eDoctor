package com.tusar.creativeitem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.tusar.creativeitem.utility.FragmentMainAppointment;

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
import com.tusar.creativeitem.utility.FragmentMainBilling;

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
    Fragment fragment;
    private ListView list;
    private TextView tvdatePicker;
    private ImageButton btnDecrement,btnIncrement;
    private long today_timestamp;
    private TextView tvAppoint;
    private ProgressDialog pDialog;
    private String chamber_id;
    private CustomAppointmentList adapter;
    ArrayList<String> name_array = new ArrayList<String>();
    ArrayList<String> phone_array = new ArrayList<String>();
    ArrayList<String> visit_array = new ArrayList<String>();
    ArrayList<String> appointment_id_array = new ArrayList<String>();
    ArrayList<String> patient_id_array = new ArrayList<String>();

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


        ArrayList<HashMap<String,String>> chamber_list;
        chamber_list = db.getAllChamber();

        for(int i=0;i<chamber_list.size();i++){
            HashMap<String,String> content = new HashMap<String, String>();
            content = chamber_list.get(i);
            String status = content.get("status");
            if(status.equals("Selected")){
                chamber_id = content.get("chamber_id");
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
                fragment = new FragmentMainAppointment();
                DatePickerDialog mDatePicker=new DatePickerDialog(AppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        selectedmonth = selectedmonth+1;
                        tvdatePicker.setText(selectedday+ "/" + selectedmonth+ "/" + selectedyear);

                        Date date = null;
                        java.text.DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = (Date)formatter.parse(tvdatePicker.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                        long today_timestamp = timeStampDate.getTime()/1000;

                        Bundle bundle = new Bundle();
                        bundle.putString("chamber_id", chamber_id);
                        bundle.putString("timestamp", String.valueOf(today_timestamp));
                        fragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragment_place2,fragment);
                        ft.commit();

                    }
                },mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });


        fragment = new FragmentMainAppointment();
        //initially sent today's date for billing list
        //today timestamp
        String today = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
        Date date = null;
        java.text.DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
        long today_timestamp = timeStampDate.getTime()/1000;

        Bundle bundle = new Bundle();
        bundle.putString("chamber_id", chamber_id);
        bundle.putString("timestamp", String.valueOf(today_timestamp));
        fragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_place2,fragment);
        ft.commit();

    }//onCreate ends here


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
