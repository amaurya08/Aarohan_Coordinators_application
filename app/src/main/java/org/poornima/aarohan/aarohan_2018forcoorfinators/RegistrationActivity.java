package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RegistrationActivity extends AppCompatActivity {
    FloatingActionButton cam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final String[] student={"Jagrati","Aaysuhi","Mayank","Kuldeep","Ashish","Bhumika","Priyam","Prashita","Harshita","hk","jk","kk","gaggu","dipti"};
        ListAdapter myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,student);
        ListView mylist = findViewById(R.id.mylist);
        mylist.setAdapter(myadapter);
        cam= findViewById(R.id.cambutton);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
    }
    }

