package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.NetWorkManager;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.registration_page_adaptor;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.RegistrationDataPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.RegistrationDetailTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "debug";
    private Button cam, logout;
    private ProgressDialog progressDialog;
    private ArrayList<RegistrationDataPojo> arrayList;
    private TextView co_email;
    private boolean back = false;
    private registration_page_adaptor registration_adapter;

    private void init() {
        arrayList = new ArrayList<>();
        co_email = findViewById(R.id.co_email);
        ListView mylist = findViewById(R.id.mylist);
        //TextView regisNo = findViewById(R.id.registration_no);
        cam = findViewById(R.id.cambutton);
        logout = findViewById(R.id.logoutbutton);
        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setCancelable(false);
        registration_adapter = new registration_page_adaptor(RegistrationActivity.this, arrayList);
        mylist.setAdapter(registration_adapter);
        progressDialog.setMessage("please wait...");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
        String emailprof = sharedPreferences.getString("email", "");
        co_email.setText(emailprof);
        progressDialog.show();
        if (NetWorkManager.checkInternetAccess(RegistrationActivity.this)) {
            listofstuRegistered();
        } else {
            progressDialog.cancel();
            Toast.makeText(RegistrationActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.if_no_entry);
            TextView noentrytxt = findViewById(R.id.noentrytxt);
            noentrytxt.setText("No Internet Connection");

        }
        methodListener();
    }

    private void listofstuRegistered() {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.securitycheckStudlist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                Log.d(TAG, "Response Recieved\n" + response);
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
                Log.d(TAG, "Unable to reach server");
                Toast.makeText(RegistrationActivity.this, "Unable to reach server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                String cidprof = sharedPreferences.getString("cid", "");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                map.put("co_id", cidprof);
                Log.d(TAG, map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )/**/
        );
        queue.add(request);
    }

    private void parseRegisteredstulist(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            Log.d(TAG, "" + message);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("message"));
            Log.d(TAG, jsonArray.toString());

            DatabaseHelper db = new DatabaseHelper(RegistrationActivity.this);
            RegistrationDetailTable.deleteTableData(db.getWritableDatabase(), "delete from " + RegistrationDetailTable.TABLE_NAME);

            Log.d(TAG, db.getDatabaseName());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectNode = jsonArray.getJSONObject(i);
                String stuname = jsonObjectNode.getString("stu_name");
                String sturegid = jsonObjectNode.getString("stu_reg_no");
                ContentValues cv = new ContentValues();
                cv.put(RegistrationDetailTable.stu_name, stuname);
                cv.put(RegistrationDetailTable.registrationId, sturegid);
                Log.d(TAG, db.getWritableDatabase().getPath());
                if (RegistrationDetailTable.insert(db.getWritableDatabase(), cv) > 0)
                    Log.d(TAG, "DATA INSERTED");
                else
                    Log.d(TAG, "Error");

            }
            arrayList.clear();
            Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + RegistrationDetailTable.TABLE_NAME, null);
            while (cursor.moveToNext()) {
                // Toast.makeText(this, ""+cursor.getColumnName(2), Toast.LENGTH_SHORT).show();
                arrayList.add(new RegistrationDataPojo(cursor.getString(0), cursor.getString(1)));
            }
            cursor.close();
            registration_adapter.notifyDataSetChanged();

        } else {
            Log.d(TAG, "Error occured with " + jsonObject.getString("message"));
            Toast.makeText(RegistrationActivity.this, "No student has been registered by you till now", Toast.LENGTH_SHORT).show();
        }
    }

    private void methodListener() {
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAPI();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", "");
                editor.putString("otp", "");
                editor.putBoolean("is", false);
                editor.putString("type", "");
                editor.apply();
                DatabaseHelper db = new DatabaseHelper(RegistrationActivity.this);
                EventCoordinatorDetailsTable.clearCoordinatorDetail(db.getWritableDatabase(), "delete from " + RegistrationDetailTable.TABLE_NAME);
                Toast.makeText(RegistrationActivity.this, "Logout Successfull", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrationActivity.this, PromptLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

 /*   private void showStudentDetail(RegistrationDataPojo student) {
        LayoutInflater factory = LayoutInflater.from(RegistrationActivity.this);
        final View dilog_view= factory.inflate(R.layout.regdialogue_student_info,null);
        final AlertDialog dialog_Event_detail = new AlertDialog.Builder(RegistrationActivity.this).create();
        dialog_Event_detail.setView(dilog_view);
        ((TextView)dilog_view.findViewById(R.id.profname)).setText(student.getStuName());
        ((TextView)dilog_view.findViewById(R.id.profreg)).setText(student.getStuId());
        ((TextView)dilog_view.findViewById(R.id.profclg)).setText(student.getStuclg());
        dialog_Event_detail.show();
       // ((ImageView)dilog_view.findViewById(R.id.myimgreg)).ssclg);
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == RC_BARCODE_CAPTURE) {
                if (data != null) {

                    String barcodeValue = data.getStringExtra("barcodeValue");
                    Log.d(TAG, "Data Scanned" + barcodeValue);
                    int flag = 0;
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getStuId().equals(barcodeValue)) {
                            Toast.makeText(RegistrationActivity.this, "Already Exist", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Already Exist");
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String timestamp = sdf.format(new Date());
                        if (NetWorkManager.checkInternetAccess(RegistrationActivity.this)) {
                            progressDialog.show();
                            registrationDetail(barcodeValue, timestamp);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Already Exist !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void registrationDetail(String barcodeValueData, String timeStampData) {
        final String timeStamp = timeStampData;
        final String value = barcodeValueData;
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.SecurityCheck, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.cancel();
                    Log.d(TAG, "Response Recieved\n" + response);
                    parseRegistrationDetail(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(RegistrationActivity.this, "Can not reach to server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                String cidprof = sharedPreferences.getString("cid", "");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                map.put("coordinatorId", cidprof);
                map.put("registrationNo", value);
                map.put("time", timeStamp);
                Log.d(TAG, map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(request);
    }

    private void parseRegistrationDetail(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        Log.d("DEBUG_TO_CHECK_1", message);
        if (message.equals("Student already checked in.")) {
            Log.d("DEBUG_TO_CHECK", jsonObject.getString("message"));
            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
        } else {
            if (error.equals("false")) {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("message"));
                //  TableMyeventsDetails.deleteTableData(db.getWritableDatabase(), "delete from " + TableMyeventsDetails.TABLE_NAME);
                JSONObject jsonObjectNode = jsonArray.getJSONObject(0);
                String student_name = jsonObjectNode.getString("student_name");
                Log.d("Name", student_name);
                String registrationId = jsonObjectNode.getString("student_registration_no");
                String student_college = jsonObjectNode.getString("student_college");
                String student_profile_image = jsonObjectNode.getString("student_profile_image");
                LayoutInflater factory = LayoutInflater.from(RegistrationActivity.this);
                Log.d(TAG, student_profile_image);
                final View dilog_view = factory.inflate(R.layout.regdialogue_student_info, null);
                final AlertDialog dialog_Event_detail = new AlertDialog.Builder(RegistrationActivity.this).create();
                dialog_Event_detail.setView(dilog_view);
                ((TextView) dilog_view.findViewById(R.id.profname)).setText(student_name);
                ((TextView) dilog_view.findViewById(R.id.profreg)).setText(registrationId);
                ((TextView) dilog_view.findViewById(R.id.profclg)).setText(student_college);

                Picasso.with(this)
                        .load("http://aarohan.poornima.org/" + student_profile_image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .resize(250, 350)
                        .into(((ImageView) dilog_view.findViewById(R.id.myimgreg)));
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.pushupin);
                animation.setDuration(500);
                dilog_view.startAnimation(animation);
                dialog_Event_detail.show();
                listofstuRegistered();

                dialog_Event_detail.setCanceledOnTouchOutside(true);

            } else {
                Log.d(TAG, "" + jsonObject.getString("message"));
                progressDialog.cancel();

                Toast.makeText(RegistrationActivity.this, "You need to sign in again", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegistrationActivity.this, PromptLoginActivity.class));
                finish();
            }
        }
    }


    private void logoutAPI() {
        final StringRequest request = new StringRequest(Request.Method.POST, URLHelper.logOut, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "LogoutApi");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error in Api");
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
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(request);
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

