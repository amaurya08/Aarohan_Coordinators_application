package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.AccommodationStudentListAdapt;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.EventStudentListAdapt;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.AccommodationStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.EventStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.AccomodationStudentTable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccomodationActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private Button checkOutBut,checkInBut;
    private ProgressDialog progressDialog;
    private ArrayList<AccommodationStudentPojo> arrayList;
    private AccommodationStudentListAdapt myadapter;
    private TextView title;
    private ListView accolist;
    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation);
        init();
        progressDialog.show();
        AccomodationListApi();
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
        String emailprof = sharedPreferences.getString("email", "");
        title.setText(emailprof);

        listeners();
    }


    private void init() {
        checkOutBut = findViewById(R.id.checkOutBut);
        checkInBut = findViewById(R.id.checkInBut);
        title = findViewById(R.id.nametext);
        accolist = findViewById(R.id.accolist);
        arrayList= new ArrayList<>();
        myadapter = new AccommodationStudentListAdapt(AccomodationActivity.this,arrayList);
        accolist.setAdapter(myadapter);
        progressDialog = new ProgressDialog(AccomodationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    private void listeners() {
        checkOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        checkInBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccomodationActivity.this, CheckInActivity.class);
              AccomodationActivity.this.startActivity(intent);
            }
        });
        checkOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccomodationActivity.this, CheckOutActivity.class);
                AccomodationActivity.this.startActivity(intent);
            }
        });

    }


    private void AccomodationListApi() {

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.AccommodationList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseAccoStudentList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Success in Api Link");
                Toast.makeText(AccomodationActivity.this, "Success in Api Link", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.d(TAG, "Error in Api Link");
                Toast.makeText(AccomodationActivity.this, "Error in Api Link", Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                return map;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(AccomodationActivity.this);
        queue.add(request);
    }

    private void parseAccoStudentList(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            JSONArray studentArray = new JSONArray(message);
            Log.d(TAG, "" + message);
            DatabaseHelper db = new DatabaseHelper(AccomodationActivity.this);
            Log.d(TAG, "After DB Helper");
               AccomodationStudentTable.deleteTableData(db.getWritableDatabase(), "delete from " + AccomodationStudentTable.TABLE_NAME);
            //  Log.d(TAG,"Query fired DB Helper with length "+studentArray.length());
            /*Log.d(TAG,studentArray.toString());*/


            for (int i = 0; i < studentArray.length(); i++) {
                JSONObject jsonObject1 = studentArray.getJSONObject(i);
                String stu_name = jsonObject1.getString("stu_name");
                //  Log.d(TAG,stu_name);
                String stu_reg_no = jsonObject1.getString("stu_reg_no");
                String room = jsonObject1.getString("room");
                String rs_payment_status = jsonObject1.getString("rs_payment_status");
                String rc_check_in = jsonObject1.getString("rc_check_in");
                //   Log.d(TAG,rc_check_in);
                String rc_check_out = jsonObject1.getString("rc_check_out");
                //Log.d(TAG,studentArray.getJSONObject(i).toString());
                Log.d(TAG, stu_name + "\n" + stu_reg_no + "\n" + room + "\n" + rs_payment_status + "\n" + rc_check_in + "\n" + rc_check_out);

                ContentValues cv = new ContentValues();
                cv.put(AccomodationStudentTable.Stu_name, stu_name);
                cv.put(AccomodationStudentTable.Stu_reg_no, stu_reg_no);
                cv.put(AccomodationStudentTable.Room, room);
                cv.put(AccomodationStudentTable.Rs_payment_status, rs_payment_status);
                cv.put(AccomodationStudentTable.Rc_check_in, rc_check_in);
                cv.put(AccomodationStudentTable.Rc_check_out, rc_check_out);
                Toast.makeText(AccomodationActivity.this, "Database contain data", Toast.LENGTH_LONG).show();
                if (AccomodationStudentTable.insert(db.getWritableDatabase(), cv) > 0)
                    Log.d(TAG, "Databse insert");
                else
                    Log.d(TAG, "Databse Insertion Error");
            }

            fetchStuData();
        } else {
            progressDialog.cancel();
            Toast.makeText(AccomodationActivity.this, message, Toast.LENGTH_LONG).show();

        }
    }

    private void fetchStuData() {

        DatabaseHelper db = new DatabaseHelper(AccomodationActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + AccomodationStudentTable.TABLE_NAME, null);
        Log.d(TAG, "Reading DAtabase" + cursor.getCount());
        Log.d(TAG, "Cursor is not null");

        while (cursor.moveToNext()) {
            arrayList.add(new AccommodationStudentPojo(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));

        }
        myadapter.notifyDataSetChanged();
        cursor.close();

        Log.d(TAG, "I am here");
        progressDialog.cancel();

        Toast.makeText(AccomodationActivity.this, "set text succeess", Toast.LENGTH_LONG).show();

    }


}


