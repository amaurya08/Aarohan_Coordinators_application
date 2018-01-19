package org.poornima.aarohan.aarohan_2018forcoorfinators;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.app.ActionBar;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.registration_page_adaptor;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.RegistrationDataPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.RegistrationDetailTable;
public class RegistrationActivity extends AppCompatActivity {

    FloatingActionButton cam;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private ListView mylist;
    private ProgressDialog progressDialog;
    private ArrayList<RegistrationDataPojo> arrayList;
    private TextView co_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
        String emailprof = sharedPreferences.getString("email", "");
        co_email.setText(emailprof);
        progressDialog.show();
        listofstuRegistered();
        methodListener();
    }

    private void listofstuRegistered() {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.securitycheckStudlist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                Log.d("DEBUG","Response Recieved\n"+response);
                try {
                    parseRegisteredstulist(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(RegistrationActivity.this, "Error in loading list of registered student", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                String cidprof = sharedPreferences.getString("cid","");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                map.put("co_id",cidprof);
                Log.d("DEBUG",map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(request);
    }
     private void parseRegisteredstulist(String response) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            Log.d("DEBUG",""+message);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("message"));
            Log.d("DEBUG", jsonArray.toString());
            DatabaseHelper db = new DatabaseHelper(RegistrationActivity.this);
            //RegistrationDetailTable.deleteTableData(db.getWritableDatabase(), "delete from " + TableMyeventsDetails.TABLE_NAME);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObjectNode = jsonArray.getJSONObject(i);
                String stuname = jsonObjectNode.getString("stu_name");
                String sturegid = jsonObjectNode.getString("stu_reg_no");
                ContentValues cv = new ContentValues();
                cv.put(RegistrationDetailTable.stu_name, stuname);
                cv.put(RegistrationDetailTable.registrationId, sturegid);
                long j = RegistrationDetailTable.insert(db.getWritableDatabase(), cv);
                Log.d("DEBUG", "DATA INSERTED" + j);
            }
                Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + RegistrationDetailTable.TABLE_NAME, null);
                while(cursor.moveToNext()){
                    arrayList.add(new RegistrationDataPojo(cursor.getString(0),cursor.getString(1)));
                }
                cursor.close();
                registration_page_adaptor registration_adapter=new registration_page_adaptor(RegistrationActivity.this,arrayList);
                mylist.setAdapter(registration_adapter);

            }

        else
        {
            Log.d("DEBUG",""+jsonObject.getString("message"));
            Toast.makeText(RegistrationActivity.this,"No student has been registered by you till now",Toast.LENGTH_SHORT).show();
        }

    }


    private void methodListener() {
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RegistrationActivity.this,BarcodeCaptureActivity.class);
                startActivityForResult(intent,RC_BARCODE_CAPTURE);
            }
        });
    }

    private void init() {
        arrayList = new ArrayList<>();
        co_email=findViewById(R.id.co_email);
        mylist=findViewById(R.id.mylist);
        cam= findViewById(R.id.cambutton);
        progressDialog=new ProgressDialog(RegistrationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please wait...");
    }

    @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();
            if (id == R.id.action_logout)
            {
                logoutAPI();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email","");
                editor.putString("otp","");
                editor.putBoolean("is",false);
                editor.putString("type","");
                editor.apply();
                DatabaseHelper db = new DatabaseHelper(RegistrationActivity.this);
                EventCoordinatorDetailsTable.clearCoordinatorDetail(db.getWritableDatabase(), "delete from " + RegistrationDetailTable.TABLE_NAME);
                Toast.makeText(RegistrationActivity.this,"LogoutSuccessfull",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrationActivity.this,PromptLoginActivity.class);
                startActivity(intent);
                finish();

            }
                return true;
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
                            Toast.makeText(RegistrationActivity.this,"Already Exist",Toast.LENGTH_LONG).show();
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
                        registrationDetail(barcodeValue,timestamp);
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
    private void registrationDetail(String barcodeValueData,String timeStampData) {
        final String timeStamp = timeStampData;
        final String value = barcodeValueData;
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.SecurityCheck, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                    progressDialog.cancel();
                    Log.d("DEBUG","Response Recieved\n"+response);
                    parseRegistrationDetail(response,value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(RegistrationActivity.this, "Error in loading details of registration", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                String cidprof = sharedPreferences.getString("cid","");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                map.put("coordinatorId",cidprof);
                map.put("registrationNo",value);
                map.put("time",timeStamp);
                Log.d("DEBUG",map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(request);
    }
    private void parseRegistrationDetail(String response,String regid) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            Log.d("DEBUG",""+message);
            //Inserting Into Database
            DatabaseHelper db = new DatabaseHelper(RegistrationActivity.this);
            RegistrationDetailTable.deleteTableData(db.getWritableDatabase(), "delete from " + RegistrationDetailTable.TABLE_NAME);
            ContentValues cv = new ContentValues();
            cv.put(RegistrationDetailTable.stu_name,message);
            cv.put(RegistrationDetailTable.registrationId,regid);
            long j= RegistrationDetailTable.insert(db.getWritableDatabase(), cv);
            //Adding data into list view
            Cursor cursor= db.getReadableDatabase().rawQuery("SELECT * FROM " + RegistrationDetailTable.TABLE_NAME  , null);
            while(cursor.moveToNext()){
                arrayList.add(new RegistrationDataPojo(cursor.getString(0),cursor.getString(1)));
            }
            cursor.close();
            registration_page_adaptor registration_adapter=new registration_page_adaptor(RegistrationActivity.this,arrayList);
            mylist.setAdapter(registration_adapter);
        }
        else
        {
            Log.d("DEBUG",""+jsonObject.getString("message"));
            Toast.makeText(RegistrationActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void logoutAPI() {
        final StringRequest request = new StringRequest(Request.Method.POST, URLHelper.logOut, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Debug", "LogoutApi");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Debug","Error in Api");
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan",MODE_PRIVATE);
                String emailprof=sharedPreferences.getString("email","");
                String otpprof = sharedPreferences.getString("otp","");
                map.put("email",emailprof);
                map.put("otp",otpprof);
                map.put("type","COORDINATOR");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(request);
    }
}

