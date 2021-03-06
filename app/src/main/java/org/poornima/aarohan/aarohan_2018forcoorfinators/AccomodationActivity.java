package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.NetWorkManager;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.AccommodationStudentListAdapt;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.AccommodationStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.AccomodationStudentTable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccomodationActivity extends AppCompatActivity {

    private Button checkOutBut, checkInBut;
    private ProgressDialog progressDialog;
    private Button Logout;
    private ArrayList<AccommodationStudentPojo> arrayList;
    private AccommodationStudentListAdapt myadapter;
    private ListView accolist;
    private boolean back = false;
    private TextView title;
    private static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation);
        init();
        if (NetWorkManager.checkInternetAccess(AccomodationActivity.this)) {
            progressDialog.show();
            AccomodationListApi();
        } else {
            Toast.makeText(AccomodationActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.if_no_entry);
            TextView noentrytxt = findViewById(R.id.noentrytxt);
            noentrytxt.setText("No Internet Connection");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
        String emailprof = sharedPreferences.getString("email", "");
        title.setText(emailprof);

        listeners();
    }


    private void init() {
        checkOutBut = findViewById(R.id.checkOutBut);
        checkInBut = findViewById(R.id.checkInBut);
        title = findViewById(R.id.nametext);
        Logout = findViewById(R.id.logoutBut1);
       accolist = findViewById(R.id.accolist);
        arrayList = new ArrayList<>();
        myadapter = new AccommodationStudentListAdapt(AccomodationActivity.this, arrayList);
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
                finish();
            }
        });
        checkOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccomodationActivity.this, CheckOutActivity.class);
                AccomodationActivity.this.startActivity(intent);
                finish();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checksession()) {
                    logoutAPI();
                    SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", "");
                    editor.putString("otp", "");
                    editor.putString("cid", "");
                    editor.putBoolean("is", false);
                    editor.putString("type", "");
                    editor.apply();
                    DatabaseHelper db = new DatabaseHelper(AccomodationActivity.this);
                    AccomodationStudentTable.clearCoordinatorDetail(db.getWritableDatabase(), "delete from " + AccomodationStudentTable.TABLE_NAME);
                    Toast.makeText(AccomodationActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AccomodationActivity.this, PromptLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(AccomodationActivity.this, PromptLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
       accolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String regno1 = arrayList.get(i).getStu_reg_no();
                DatabaseHelper db = new DatabaseHelper(AccomodationActivity.this);

                Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + AccomodationStudentTable.TABLE_NAME, null);

                while (cursor.moveToNext()) {
                    if (cursor.getString(1).equals(regno1)) {
                        String Name = "Name:-" + cursor.getString(0);
                        String Room = "Room Detail:-" + cursor.getString(2);
                        String Regno = "Registration No.:-" + cursor.getString(1);
                        String checkin = "Check In Time:-" + cursor.getString(4);
                        String checkout = "Check Out Time:-" + cursor.getString(5);
                        Log.d("DEBUG", Name + ".." + Room + ".." + Regno + ",," + checkin + ".." + checkout);

                        View dialogview = getLayoutInflater().inflate(R.layout.dialog_scanner_result, null);

                        if (dialogview != null) {
                            TextView name = dialogview.findViewById(R.id.nametxt);
                            name.setText(Name);
                            ((TextView) dialogview.findViewById(R.id.roomtxt)).setText(Room);
                            ((TextView) dialogview.findViewById(R.id.regtxt)).setText(Regno);
                            ((TextView) dialogview.findViewById(R.id.checkintxt)).setText(checkin);
                            ((TextView) dialogview.findViewById(R.id.checkouttxt)).setText(checkout);
                            if (cursor.getString(3).equals("0")) {
                                String no="Payment Status:-No";
                                ((TextView) dialogview.findViewById(R.id.paymenttxt)).setText(no);
                            } else {
                                String yes="Payment Status:-Yes";
                                ((TextView) dialogview.findViewById(R.id.paymenttxt)).setText(yes);
                            }
                            AlertDialog.Builder dialogbuild = new AlertDialog.Builder(AccomodationActivity.this);
                            dialogbuild.setView(dialogview)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.cancel();

                                        }
                                    });
                            dialogbuild.setCancelable(false);
                            dialogbuild.show().create();

                        }
                    }


                }
                cursor.close();
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
                //Toast.makeText(AccomodationActivity.this, "Success in Api Link", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.d(TAG, "Error in Api Link");
                Toast.makeText(AccomodationActivity.this, "Error to  Network", Toast.LENGTH_LONG).show();

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
                // Toast.makeText(AccomodationActivity.this, "Database contain data", Toast.LENGTH_LONG).show();
                if (AccomodationStudentTable.insert(db.getWritableDatabase(), cv) > 0)
                    Log.d(TAG, "Databse insert");
                else
                    Log.d(TAG, "Databse Insertion Error");
            }

            fetchStuData();
        } else {
            progressDialog.cancel();

            Toast.makeText(AccomodationActivity.this, "You need to sign in again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AccomodationActivity.this, PromptLoginActivity.class));
            finish();


        }
    }

    private void fetchStuData() {

        DatabaseHelper db = new DatabaseHelper(AccomodationActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + AccomodationStudentTable.TABLE_NAME, null);
        Log.d(TAG, "Reading DAtabase" + cursor.getCount());
        Log.d(TAG, "Cursor is not null");

        while (cursor.moveToNext()) {
            arrayList.add(new AccommodationStudentPojo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));

        }
        myadapter.notifyDataSetChanged();
        cursor.close();

        Log.d(TAG, "I am here");
        progressDialog.cancel();

        //Toast.makeText(AccomodationActivity.this, "set text succeess", Toast.LENGTH_LONG).show();

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
                Log.d("Debug", "Error in Api");
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
                map.put("type", "COORDINATOR");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AccomodationActivity.this);
        queue.add(request);

    }


    private Boolean checksession() {
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is", false);
    }

    public void onBackPressed() {

        if(back){
            super.onBackPressed();
            return ;
        }
        this.back=true;
        Toast.makeText(this, "Please Click Twice to Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                back=false;
            }
        },2000);

    }
}


