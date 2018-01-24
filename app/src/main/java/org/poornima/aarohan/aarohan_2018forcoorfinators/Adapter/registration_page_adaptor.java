package org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.RegistrationDataPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

import java.util.ArrayList;

/**
 * Created by Bhoomika on 12-01-2018.
 */

public class registration_page_adaptor extends ArrayAdapter {
    private ArrayList arraylist;
    public registration_page_adaptor(Context context,ArrayList<RegistrationDataPojo> objects)
    {
        super(context, R.layout.registration_list_raw, objects);
        arraylist=objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myinflate = LayoutInflater.from(getContext());
        View customView = myinflate.inflate(R.layout.registration_list_raw,parent,false);
        RegistrationDataPojo sp= (RegistrationDataPojo) arraylist.get(position);
        ((TextView)customView.findViewById(R.id.stu_name)).setText(sp.getStuName());
        ((TextView)customView.findViewById(R.id.registration_no)).setText(sp.getStuId());
        return customView;
    }
}
