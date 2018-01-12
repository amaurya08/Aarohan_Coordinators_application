package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class RegistrationActivity extends AppCompatActivity {

    FloatingActionButton cam;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> myadapter;
    private ListView mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //String[] student={"Jagrati","Aaysuhi"};
        arrayList=new ArrayList<>();
        myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        mylist = findViewById(R.id.mylist);
        mylist.setAdapter(myadapter);
        cam= findViewById(R.id.cambutton);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RegistrationActivity.this,BarcodeCaptureActivity.class);
                startActivityForResult(intent,RC_BARCODE_CAPTURE);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode==RC_BARCODE_CAPTURE)
        {
            LayoutInflater factory = LayoutInflater.from(RegistrationActivity.this);
            final View dialogview= factory.inflate(R.layout.dialog_scanner_result,null);
            final AlertDialog dialog = new AlertDialog.Builder(RegistrationActivity.this).create();
            dialog.setView(dialogview);
            Button okbut = dialogview.findViewById(R.id.okbut);
            Button cancelbut = dialogview.findViewById(R.id.cancelbut);
            int flag=0;
            TextView regno = dialogview.findViewById(R.id.nametxt);
            if(resultCode== RC_BARCODE_CAPTURE)
            {
                if(data!=null)
                {
                    Log.d("Data", data.getStringExtra("barcodeValue"));
                    final String barcodeValue = data.getStringExtra("barcodeValue");
                    regno.setText(barcodeValue);
                    for(int i=0;i<arrayList.size();i++)
                    {
                        if(arrayList.get(i).equals(barcodeValue))
                        {
                            Toast.makeText(RegistrationActivity.this,"Already Exist",Toast.LENGTH_LONG).show();
                            flag=1;
                            break;
                        }
                        else
                        {
                            flag=0;
                        }
                    }
                    if(flag==0)
                    {
                        dialog.show();
                        okbut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                arrayList.add(barcodeValue);
                                myadapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        cancelbut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(RegistrationActivity.this,"Data not Saved",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

