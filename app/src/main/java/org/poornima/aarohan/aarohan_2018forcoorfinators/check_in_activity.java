package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.registration_page_adaptor;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.RegistrationDataPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.RegistrationDetailTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhoomika on 19-01-2018.
 */

public class check_in_activity extends AppCompatActivity {
    FloatingActionButton cam;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private ListView mylist;
    private ProgressDialog progressDialog;
    private ArrayList<RegistrationDataPojo> arrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);
        init();
        listofstuCheckin();
        methodListener();
    }

    private void listofstuCheckin() {
    }

    private void init() {
        arrayList = new ArrayList<>();
        mylist=findViewById(R.id.mylist);
        cam= findViewById(R.id.cambutton);
        progressDialog=new ProgressDialog(check_in_activity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please wait...");
    }
    void  methodListener()
    {
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(check_in_activity.this,BarcodeCaptureActivity.class);
                startActivityForResult(intent,RC_BARCODE_CAPTURE);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RC_BARCODE_CAPTURE)
        {
            if(resultCode== RC_BARCODE_CAPTURE)
            {
                if(data!=null)
                {

                    String barcodeValue = data.getStringExtra("barcodeValue");
                    Log.d("DEBUG","Data Scanned"+ barcodeValue);
                    int flag = 0;
                    for(int i=0;i<arrayList.size();i++)
                    {
                        if(arrayList.get(i).getStuId().equals(barcodeValue))
                        {
                            Toast.makeText(check_in_activity.this,"Already Exist",Toast.LENGTH_LONG).show();
                            Log.d("DEBUG","Already Exist");
                            flag=1;
                            break;
                        }
                    }
                    if(flag==0)
                    {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String timestamp =sdf.format(new Date());
                        progressDialog.show();
                        checkinDetail(barcodeValue,timestamp);
                    }
                    else
                    {
                        Toast.makeText(this, "Already Exist !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkinDetail(String barcodeValue, String timestamp) {
        final String timeStamp = timestamp;
        final String value = barcodeValue;
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.accommodationAttendance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.cancel();
                    Log.d("DEBUG","Response Recieved\n"+response);
                    parseCheckinDetail(response,value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(check_in_activity.this, "Error in loading detail of checkin accommodation", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                String cidprof = sharedPreferences.getString("co_id","");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                map.put("co_id",cidprof);
                map.put("stu_reg_no",value);
                map.put("time_stamp",timeStamp);
                map.put("check_type","Check In");
                Log.d("DEBUG",map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(check_in_activity.this);
        queue.add(request);
    }
    void parseCheckinDetail(String response,String value)  throws JSONException
    {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            Log.d("DEBUG",""+message);
            //Inserting Into Database

        }
    }
    }

