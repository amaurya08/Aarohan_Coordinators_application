package org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.EventStudentListAdapt;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.EventStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.R;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventStudentsTable;

import java.util.ArrayList;


public class AbsentFrag extends Fragment {
    private ArrayList<EventStudentPojo> arrayList;
    private EventStudentListAdapt myadapter;

    public AbsentFrag() {
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View customview = inflater.inflate(R.layout.frag_absent, container, false);

        arrayList = new ArrayList<>();
        myadapter = new EventStudentListAdapt(getContext(), arrayList);
        ListView mylist = customview.findViewById(R.id.List1);
        mylist.setAdapter(myadapter);
        fetchAllStudent();

        // EventStudentPojo pojo = (EventStudentPojo) arrayList.get(i)

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG).show();
            }
        });
        return customview;
    }

    private void fetchAllStudent() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from " + EventStudentsTable.TABLE_NAME + " where " + EventStudentsTable.Ev_event_att + "=" + "0", null);
        Log.d("Debug", "cursor y :" + cursor.toString());
        while (cursor.moveToNext()) {
            arrayList.add(new EventStudentPojo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));

        }
        //myAdpater.notifyDataSetChanged();
        Log.d("Debug", "arraylist");
        myadapter.notifyDataSetChanged();
        cursor.close();


    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        fetchAllStudent();


    }
}
