package org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.AccommodationStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

import java.util.ArrayList;

/**
 * Created by ADMIN on 19-Jan-18.
 */

public class AccommodationStudentListAdapt extends ArrayAdapter {
    private ArrayList arrayList;
    public AccommodationStudentListAdapt(@NonNull Context context, ArrayList<AccommodationStudentPojo> objects) {
        super(context, R.layout.list_view_adapt_event_coor,objects);
        arrayList=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customview = layoutInflater.inflate(R.layout.list_view_adapt_event_coor,parent,false);
        AccommodationStudentPojo pojo= (AccommodationStudentPojo) arrayList.get(position);
        ((TextView)customview.findViewById(R.id.eventtxt)).setText(pojo.getStu_name());
        if(pojo.getRs_payment_status().equals("0"))
        {
            ((TextView)customview.findViewById(R.id.partxt)).setText("Payment Status:-No");
        }
        else
        {
            ((TextView)customview.findViewById(R.id.partxt)).setText("Payment Status:-Yes");
        }
        return customview;
    }
}
