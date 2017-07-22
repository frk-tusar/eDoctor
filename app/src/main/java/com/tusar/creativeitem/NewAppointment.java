package com.tusar.creativeitem;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

import com.tusar.creativeitem.utility.HintSpinnerAdapter;

public class NewAppointment extends AppCompatActivity {

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

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(new HintSpinnerAdapter(
                adapter1, R.layout.hint_row_item, this));

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_schedule);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.schedule, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(new HintSpinnerAdapter(
                adapter2, R.layout.hint_row_item1, this));

        //tab2


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
