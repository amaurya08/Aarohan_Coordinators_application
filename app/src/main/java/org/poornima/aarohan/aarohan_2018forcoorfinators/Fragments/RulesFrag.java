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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



;import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.EventCoordinatorActivity;
import org.poornima.aarohan.aarohan_2018forcoorfinators.R;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;

/**
 * Created by ADMIN on 29-Dec-17.
 */

public class RulesFrag extends Fragment
{
    private TextView details;
    public RulesFrag()
    {
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View customview= inflater.inflate(R.layout.frag_rules, container, false);
        details=customview.findViewById(R.id.detailtxt);
        fetchDetails();
        return customview;
    }

    private void fetchDetails() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from "+ EventCoordinatorDetailsTable.TABLE_NAME + " WHERE " + EventCoordinatorDetailsTable.Event_name + "=?" ,new String[]{getArguments().getString("eventname")});
        Log.d("Debug","cursor :"+cursor.toString());
        //nametext.setText(EventCoordinatorDetailsTable.Co_name);

        while (cursor.moveToNext())
        {
            String s;
            s=("Title:"+cursor.getString(1))
                    .concat("Date:"+cursor.getString(7))
                    .concat("Time:"+cursor.getString(8))
                    .concat("Venue:"+cursor.getString(6))
                    .concat("Event Type"+cursor.getString(4))
                    .concat("Participation Category"+cursor.getString(3))
                    .concat("Event Category"+cursor.getString(2))
                    .concat("Description:"+cursor.getString(5));
            details.setText(s);
        }
        cursor.close();

    }
}
