package com.tusar.creativeitem;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;

import com.tusar.creativeitem.helper.DatabaseHandler;
import com.tusar.creativeitem.utility.HintSpinnerAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NewAppointment extends AppCompatActivity {
    EditText etPatName, etMobile, etDate;
    private DatabaseHandler db;
    private String chamber_id;
    private String key1=null;
    Spinner spinner1, spinner2;
    List<String> schedule_array = new ArrayList<String>();
    ArrayList<String> arr = new ArrayList<String>();
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left);
        }

        final TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        db = new DatabaseHandler(this);

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("New Patient");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Old Patient");
        host.addTab(spec);

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            host.getTabWidget().getChildAt(i).getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }



        //tab1
        etPatName = (EditText) findViewById(R.id.etPatName);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etDate = (EditText) findViewById(R.id.etDate);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        etDate.setText(date);

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(NewAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth+1;
                        etDate.setText(selectedday+ "/" + selectedmonth+ "/" + selectedyear);
                        updateschedule(etDate.getText().toString());



                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select appointment date");
                mDatePicker.show();

            }

        });

        updateschedule(date);

        spinner1 = (Spinner) findViewById(R.id.spinner_gender);
        spinner2 = (Spinner) findViewById(R.id.spinner_schedule);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(new HintSpinnerAdapter(
                adapter1, R.layout.hint_row_item, this));

        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(new HintSpinnerAdapter(
                adapter2, R.layout.hint_row_item1, this));

        //tab2


    }//onCreate ends here
    public void updateschedule(String input_date){
        arr = new ArrayList<String>();
        ArrayList<HashMap<String,String>> chamber_list = db.getAllChamber();
        for(int i=0;i<chamber_list.size();i++){
            HashMap<String,String> content = new HashMap<String, String>();
            content = chamber_list.get(i);
            String status = content.get("status");
            if(status.equals("Selected")){
                chamber_id = content.get("chamber_id");
            }
        }

        ArrayList<HashMap<String,String>> schedule_list = db.getScheduleById(chamber_id);
        for(int j=0;j<schedule_list.size();j++){
            HashMap<String,String> content1 = new HashMap<String, String>();
            content1 = schedule_list.get(j);
            String key = content1.get("key");


            SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
            Date dt1= null;
            try {
                dt1 = format1.parse(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat format2=new SimpleDateFormat("EEEE");
            String date=format2.format(dt1);
            System.out.println("day: "+ date);

            if(date.equals("Sunday")){
                key1 = String.valueOf(0);
            }else if(date.equals("Monday")){
                key1 = String.valueOf(1);
            }else if(date.equals("Tuesday")){
                key1 = String.valueOf(2);
            }else if(date.equals("Wednesday")){
                key1 = String.valueOf(3);
            }else if(date.equals("Thursday")){
                key1 = String.valueOf(4);
            }else if(date.equals("Friday")){
                key1 = String.valueOf(5);
            }else if(date.equals("Saturday")){
                key1 = String.valueOf(6);
            }

            System.out.println("Test key1: "+key1);
            System.out.println("Test key: "+key);
            if(key1.equals(key)){
                System.out.println("Hello World");
                String morning = content1.get("morning");
                String afternoon = content1.get("afternoon");
                String evening = content1.get("evening");

                if(morning != null){
                    arr.add(morning);
                }
                if(afternoon != null){
                    arr.add(afternoon);
                }
                if(evening != null){
                    arr.add(evening);
                }
            }
            System.out.println("Testing "+arr);
        }




    }

    // for back button in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // for edit button in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tick, menu);
        return true;
    }
}
