package com.tusar.creativeitem;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tusar.creativeitem.helper.DatabaseHandler;

import java.util.HashMap;

public class ProfileActivity extends BaseActivity {
    private DatabaseHandler db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(6).setChecked(true);

        TextView tvName = (TextView) findViewById(R.id.tvDrName);

        final EditText etName = (EditText) findViewById(R.id.etDocName);
        final EditText etPhone = (EditText) findViewById(R.id.etDocMobile);
        final EditText etEmail = (EditText) findViewById(R.id.etDocEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etDocPassword);
        final EditText etDegree = (EditText) findViewById(R.id.etDocDegree);

        ImageButton imgName = (ImageButton) findViewById(R.id.imgDocName);
        ImageButton imgPhone = (ImageButton) findViewById(R.id.imgDocPhone);
        ImageButton imgEmail = (ImageButton) findViewById(R.id.imgDocEmail);
        ImageButton imgPassword = (ImageButton) findViewById(R.id.imgDocPassword);
        ImageButton imgDegree = (ImageButton) findViewById(R.id.imgDocDegree);

        db = new DatabaseHandler(this);
        // get data from sqlite
        HashMap<String, String> user = db.getUser();
        final String name = user.get("name");
        final String phone = user.get("phone");
        final String email = user.get("email");

        tvName.setText(name);

        etName.setText(name);
        etPhone.setText(phone);
        etEmail.setText(email);
        etPassword.setText("********");
        etDegree.setText("MBBS,FRCS,FCPS(London),Department Head(Dhaka Medical College)");

        imgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etName.setCursorVisible(true);
            }
        });
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhone.setText("");
                etPhone.setCursorVisible(true);
            }
        });
        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
                etEmail.setCursorVisible(true);
            }
        });
        imgPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.setText("");
                etPassword.setCursorVisible(true);
            }
        });
        imgDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDegree.setText("");
                etDegree.setCursorVisible(true);
            }
        });
    }
}
