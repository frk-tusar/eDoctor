package com.tusar.creativeitem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tusar.creativeitem.helper.DatabaseHandler;

import java.util.HashMap;


public class MainActivity extends BaseActivity {

    private TextView mTextMessage;

    private com.github.clans.fab.FloatingActionMenu menuRed;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    private com.github.clans.fab.FloatingActionButton fab3;
    private com.github.clans.fab.FloatingActionButton fab4;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);



        db = new DatabaseHandler(this);

        mTextMessage = (TextView) findViewById(R.id.message);

        // get data from sqlite
        HashMap<String, String> user = db.getUser();
        final String name = user.get("name");
        mTextMessage.setText("Welcome, "+ name);


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
                Intent i = new Intent(MainActivity.this, NewAppointment.class);
                startActivity(i);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PrescriptionActivity.class);
                startActivity(i);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewPatient.class);
                startActivity(i);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewBilling.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Exit")
                .setMessage("Do you want to close the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
