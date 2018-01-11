package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.main_activity_adapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String [] coordinator={"Registration Desk","Event Coordinator","Accomodation Desk"};
        final String [] modules={"module1","module2","module3"};
        ListAdapter myadapter = new main_activity_adapter(MainActivity.this,coordinator);
        ListView listCoordinator = findViewById(R.id.mainlist);
        listCoordinator.setAdapter(myadapter);

        listCoordinator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               /* String coor=coordinator[i];

                if(coor.equals("Event Coordinator"))
                {
                    Intent intent = new Intent(MainActivity.this,EventCoordinatorActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this,coor,Toast.LENGTH_LONG).show();*/
               startModule(modules[i]);
            }
        });
    }

    private void startModule(String module) {
        Intent intent = new Intent(MainActivity.this, PromptLoginActivity.class);
        intent.putExtra("modulename",module);
        startActivity(intent);
    }


}
