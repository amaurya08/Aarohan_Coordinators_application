package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;

import java.util.ArrayList;

//TODO Profile Activity
//TODO  Events Name in List View
//TODO Event Listenenr invoke EvenAllDetailsActivit
public class EventCoordinatorActivity extends AppCompatActivity {
    private ListView evelist;
    private TextView nametext;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_coordinator);
        init();

        //setCoordinatorDetails();
        //String [] events = {"Football","Volleyball","GroupDance","SoloDance"};
        arrayList=new ArrayList<>();
        ListAdapter myadapter = new ArrayAdapter<String>(EventCoordinatorActivity.this,android.R.layout.simple_list_item_1,arrayList);
        evelist.setAdapter(myadapter);
        fetchCoordinatorDetails();
        methodListener();

    }



    private void fetchCoordinatorDetails() {

        DatabaseHelper db = new DatabaseHelper(EventCoordinatorActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from "+ EventCoordinatorDetailsTable.TABLE_NAME,null);
        Log.d("Debug","cursor :"+cursor.toString());
        //nametext.setText(EventCoordinatorDetailsTable.Co_name);

        while (cursor.moveToNext())
        {
            Log.d("Msg",cursor.getString(1));
            arrayList.add(cursor.getString(1));
            nametext.setText(cursor.getString(9));
        }
        cursor.close();
    }

    private void methodListener() {
        evelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EventCoordinatorActivity.this,EventAllDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        evelist = findViewById(R.id.eventlist);
        nametext = findViewById(R.id.nametxt);
    }
}
