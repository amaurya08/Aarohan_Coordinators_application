package org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

/**
 * Created by Bhoomika on 10-01-2018.
 */

public class AbsentAccoFrag extends Fragment {
    public AbsentAccoFrag()
    {
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View customview= inflater.inflate(R.layout.frag_absent, container, false);

        final String [] student={"Jagrati","Aaysuhi","Mayank","Kuldeep Sir","Ashish Sir","Bhumika","Priyam","Prashita","Harshita","hk","jk","kk"};
        ListAdapter myadapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,student);
        ListView mylist =customview.findViewById(R.id.List1);
        mylist.setAdapter(myadapter);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String students = student[i];
                Toast.makeText(getActivity(),students,Toast.LENGTH_LONG).show();
            }
        });
        return customview;
    }
}