package org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.EventStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

import java.util.ArrayList;



public class EventStudentListAdapt extends ArrayAdapter {

    private ArrayList arrayList;
    public EventStudentListAdapt(@NonNull Context context, ArrayList<EventStudentPojo> objects) {
        super(context, R.layout.list_view_adapt_event_coor,objects);
        arrayList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customview = layoutInflater.inflate(R.layout.list_view_adapt_event_coor,parent,false);
        EventStudentPojo pojo= (EventStudentPojo) arrayList.get(position);
        ((TextView)customview.findViewById(R.id.eventtxt)).setText(pojo.getStu_name());
        ((TextView)customview.findViewById(R.id.partxt)).setText(pojo.getStu_reg_no());
        return customview;
    }
}
