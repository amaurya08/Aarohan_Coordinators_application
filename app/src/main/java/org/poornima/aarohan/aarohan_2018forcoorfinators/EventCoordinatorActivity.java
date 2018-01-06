package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

//TODO Profile Activity
//TODO  Events Name in List View
//TODO Event Listenenr invoke EvenAllDetailsActivit
public class EventCoordinatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_coordinator);

        String [] events = {"Football","Volleyball","GroupDance","SoloDance"};

        ListAdapter myadapter = new ArrayAdapter<String>(EventCoordinatorActivity.this,android.R.layout.simple_list_item_1,events);
        ListView evelist = findViewById(R.id.eventlist);
        evelist.setAdapter(myadapter);

        evelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EventCoordinatorActivity.this,EventAllDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
