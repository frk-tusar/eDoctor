package com.tusar.creativeitem.utility;

/**
 * Created by Farruck Ahmed Tusar on 10-Jun-17.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tusar.creativeitem.BaseActivity;
import com.tusar.creativeitem.R;

import java.util.ArrayList;

public class CustomGridviewAdapter extends BaseAdapter{
    ArrayList<String> result;
    Context context;
    private static LayoutInflater inflater=null;
    public CustomGridviewAdapter(BaseActivity mainActivity, ArrayList<String> osNameList) {
        // TODO Auto-generated constructor stub
        osNameList.add("Add");
        result=osNameList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView os_text;
        ImageView os_images;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.gridlayout, null);
        holder.os_text =(TextView) rowView.findViewById(R.id.gv_texts);
        holder.os_images = (ImageView) rowView.findViewById(R.id.gv_images);

        holder.os_text.setText(result.get(position));
        if(result.get(position).equals("Add")){
            holder.os_images.setImageResource(R.mipmap.plus);
        }

//        rowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked at"+ result.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });

        return rowView;
    }

}
