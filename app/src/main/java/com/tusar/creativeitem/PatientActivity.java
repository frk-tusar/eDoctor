package com.tusar.creativeitem;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.tusar.creativeitem.helper.DatabaseHandler;
import com.tusar.creativeitem.utility.CustomPatientList;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientActivity  extends BaseActivity {

    private com.github.clans.fab.FloatingActionMenu menuRed;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    private com.github.clans.fab.FloatingActionButton fab3;
    private com.github.clans.fab.FloatingActionButton fab4;

    private DatabaseHandler db;
    private ListView list;
    private String name1,phone1,patient_id;

    ArrayList<String> name_array = new ArrayList<String>();
    ArrayList<String> phone_array = new ArrayList<String>();
    ArrayList<String> p_id_array = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_patient, contentFrameLayout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);

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
                Intent i = new Intent(PatientActivity.this, NewAppointment.class);
                startActivity(i);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PatientActivity.this, PrescriptionActivity.class);
                startActivity(i);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PatientActivity.this, NewPatient.class);
                startActivity(i);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PatientActivity.this, NewBilling.class);
                startActivity(i);
            }
        });


        //from sqlite
        ArrayList<HashMap<String,String>> new_list;
        new_list = db.getAllPatient();

        // If no data available in table
        if(new_list.size()==0){
            System.out.println("No data");
        }
        else {
            for(int i=0;i<new_list.size();i++){
                HashMap<String,String> content = new HashMap<String, String>();
                content = new_list.get(i);
                name1 = content.get("name");
                phone1 = content.get("phone");
                patient_id = content.get("patient_id");
                name_array.add(name1);
                phone_array.add(phone1);
                p_id_array.add(patient_id);
            }
        }
        //listview
        CustomPatientList adapter = new
                CustomPatientList(PatientActivity.this, name_array, phone_array);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(PatientActivity.this, PatientDetailsActivity.class);
                i.putExtra("name", name_array.get(position));
                i.putExtra("patient_id", p_id_array.get(position));
                startActivity(i);
            }
        });

    } //onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
