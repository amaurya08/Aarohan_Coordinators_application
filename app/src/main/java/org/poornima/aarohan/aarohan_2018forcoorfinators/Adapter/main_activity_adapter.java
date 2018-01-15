package org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

/**
 * Created by ADMIN on 06-Jan-18.
 */

public class main_activity_adapter extends ArrayAdapter<String> {

    public main_activity_adapter(@NonNull Context context,String [] Coordinator) {
        super(context, R.layout.list_view_adapt_main_activity, Coordinator);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       LayoutInflater myinflate = LayoutInflater.from(getContext());
       View customView = myinflate.inflate(R.layout.list_view_adapt_main_activity,parent,false);
     //   ((TextView)customView.findViewById(R.id.stu_name)).setText();
       // ((TextView)customView.findViewById(R.id.registration_no)).setText();
        return customView;
    }
}
