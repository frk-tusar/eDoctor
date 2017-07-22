package com.tusar.creativeitem.utility;

/**
 * Created by Farruck Ahmed Tusar on 20-Jun-17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.tusar.creativeitem.R;

import java.util.ArrayList;

public class CustomAppointmentList extends ArrayAdapter<String>{

    private final Activity context;
    private ArrayList<String> name_array = new ArrayList<String>();
    private ArrayList<String> phone_array = new ArrayList<String>();

    public CustomAppointmentList(Activity context, ArrayList<String> name_array, ArrayList<String> phone_array) {
        super(context, R.layout.list_single2, name_array);
        this.context = context;
        this.name_array = name_array;
        this.phone_array = phone_array;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single2, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.txt2);
        TextView txtPhone = (TextView) rowView.findViewById(R.id.txt3);
        final ImageButton imgMore = (ImageButton) rowView.findViewById(R.id.imgMore);
        final ImageButton imgCheck = (ImageButton) rowView.findViewById(R.id.imgCheck);

        txtName.setText(name_array.get(position));
        txtPhone.setText(phone_array.get(position));
        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Manage Profile", "Manage Prescription", "Mark as visited"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if(item == 0) {

                        } else if(item == 1) {

                        } else if(item == 2) {
                            imgCheck.setImageResource(R.drawable.ic_check_circle_active);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                dialog.show();


//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(context, imgMore);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popup_memu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(context,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//
//                popup.show();//showing popup menu
            }
        });

        return rowView;
    }
}
