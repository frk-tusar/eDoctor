package com.tusar.creativeitem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
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
import com.tusar.creativeitem.utility.HintSpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tusar.creativeitem.R.id.etDate;


public class PrescriptionActivity extends BaseActivity{

    private TableLayout tableLayout, tableLayout1, tableLayout2, tableLayout3;
    private DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_prescription, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

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

        db = new DatabaseHandler(this);

        //tab1

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(new HintSpinnerAdapter(
                adapter, R.layout.hint_row_item, this));

        //Table View codes here
        tableLayout2=(TableLayout)findViewById(R.id.tableLayoutSymptoms);
        final EditText etSymptoms = (EditText) findViewById(R.id.etSymptoms);
        ImageButton btnSymptoms = (ImageButton) findViewById(R.id.btnSymptom);

        btnSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSymptoms.requestFocus();
            }
        });


        //Table View codes here
        tableLayout3=(TableLayout)findViewById(R.id.tableLayoutDiagnosis);
        final EditText etDiagnosis = (EditText) findViewById(R.id.etDiagnosis);
        ImageButton btnDiagnosis = (ImageButton) findViewById(R.id.btnDiagnosis);

        btnDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDiagnosis.requestFocus();
            }
        });

        //Table View codes here
        tableLayout=(TableLayout)findViewById(R.id.tableLayoutMedicine);
        final View tableRow = LayoutInflater.from(PrescriptionActivity.this).inflate(R.layout.table_medicine_row,null,false);

        final TextView medicineName  = (TextView) tableRow.findViewById(R.id.tvMedicineName);
        final TextView medicineDesc  = (TextView) tableRow.findViewById(R.id.tvMedicineDes);
        ImageButton delete = (ImageButton) tableRow.findViewById(R.id.btndelete);

        ImageButton imgMedicine = (ImageButton) findViewById(R.id.btnMedicine);
        imgMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
                builder.setView(R.layout.popup_addmedicine);
                builder.setTitle("Add Medicine");
                final AlertDialog dialog = builder.create();
                dialog.show();

                final EditText etMedicineName = (EditText) dialog.findViewById(R.id.etMedicineName);
                final EditText etMedicineDesc = (EditText) dialog.findViewById(R.id.etMedicineDesc);
                Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                Button buttonConfirm = (Button) dialog.findViewById(R.id.buttonConfirm);

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Name, Desc = null;

                        Name = etMedicineName.getText().toString().trim();
                        Desc = etMedicineDesc.getText().toString().trim();

                        if (etMedicineName.getText().toString().length() == 0) {
                            etMedicineName.setError("Name cannot be blank");
                            return;
                        }
                        else if (etMedicineDesc.getText().toString().length() == 0) {
                            etMedicineDesc.setError("Description cannot be blank");
                            return;
                        }
                        else {

                            medicineName.setText(Name);
                            medicineDesc.setText(Desc);

                            tableLayout.addView(tableRow);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        //Table View codes here
        tableLayout1=(TableLayout)findViewById(R.id.tableLayoutTest);
        final View tableRow1 = LayoutInflater.from(PrescriptionActivity.this).inflate(R.layout.table_test_row,null,false);

        final TextView testName  = (TextView) tableRow1.findViewById(R.id.tvTestName);
        ImageButton delete1 = (ImageButton) tableRow1.findViewById(R.id.btndelete);

        ImageButton imgTest = (ImageButton) findViewById(R.id.btnTest);
        imgTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
                builder.setView(R.layout.popup_addtest);
                builder.setTitle("Add Test");
                final AlertDialog dialog = builder.create();
                dialog.show();

                final EditText etTestName = (EditText) dialog.findViewById(R.id.etTestName);
                Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                Button buttonConfirm = (Button) dialog.findViewById(R.id.buttonConfirm);

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        etTestName.getText().toString().trim();

                        if (etTestName.getText().toString().length() == 0) {
                            etTestName.setError("Name cannot be blank");
                            return;
                        }
                        else {
                            testName.setText(etTestName.getText().toString().trim());
                            tableLayout1.addView(tableRow1);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });



        //tab2
        //getPrescription();

    }

    public void getPrescription(){

        RequestQueue queue = Volley.newRequestQueue(PrescriptionActivity.this);
        String url = AppConfigURL.URL + "get_prescription_history_of_patient";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //split to string from json response
                        System.out.println("Res: "+response);
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                String patient_id     = jsonObject.getString("patient_id");
//                                String name     = jsonObject.getString("name");
//                                String phone     = jsonObject.getString("phone");
//                                String address     = jsonObject.getString("address");
//                                String about     = jsonObject.getString("about");
//                                String age     = jsonObject.getString("age");
//                                String gender     = jsonObject.getString("gender");
//
//                                // add data into sqlite
//                                db.addPatient(patient_id,name,phone,address,about,age,gender);
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error Toast
                Toast.makeText(getApplicationContext(),"Error Response",Toast.LENGTH_LONG).show();

            }
        }) {
            //adding parameters to the request 01717255741
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // get data from sqlite
                HashMap<String, String> user = db.getUser();
                final String token = user.get("token");
                final String user_id = user.get("user_id");
                final String account_id = user.get("account_id");

                Map<String, String> params = new HashMap<>();
                params.put("auth_token", token);
                params.put("user_id", user_id);
                params.put("patient_id", account_id);
                params.put("authenticate", "true");

                return params;

            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tick, menu);
        return true;
    }

}
