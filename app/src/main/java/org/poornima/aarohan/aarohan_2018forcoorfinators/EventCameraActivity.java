package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventStudentsTable;

import java.util.HashMap;
import java.util.Map;

public class EventCameraActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Button scan;
    private Button stuList;
    private TextView evetxt;
    private TextView ruletxt;
    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_camera);
        init();
        studentsOfEventAPI();
        //evetxt.setText(getIntent().getStringExtra("eventid"));
        ruleSetText();
        listener();
    }


    private void init() {
        scan = findViewById(R.id.scanbut);
        stuList = findViewById(R.id.stulist);
        evetxt = findViewById(R.id.evename);
        ruletxt = findViewById(R.id.ruletxt);
        progressDialog = new ProgressDialog(EventCameraActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

    }

    private void ruleSetText() {
        String eveid = getIntent().getStringExtra("eventid");

        DatabaseHelper db = new DatabaseHelper(EventCameraActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from " + EventCoordinatorDetailsTable.TABLE_NAME + " WHERE " + EventCoordinatorDetailsTable.Event_id + "=?", new String[]{eveid});
        Log.d("Debug", "cursor :" + cursor.toString());
        //nametext.setText(EventCoordinatorDetailsTable.Co_name);

        while (cursor.moveToNext()) {
            evetxt.setText(cursor.getString(1));
            String s;
            s = ("Date:-" + cursor.getString(7))
                    .concat("<p>" + "Time:-" + cursor.getString(8) + "</p>")
                    .concat("<p>" + "Venue:-" + cursor.getString(6) + "</p>")
                    .concat("<p>" + "Event Type:-" + cursor.getString(4) + "</p>")
                    .concat("<p>" + "Participation Category:-" + cursor.getString(3) + "</p>")
                    .concat("<p>" + "Event Category:-" + cursor.getString(2) + "</p>")
                    .concat("<p>" + "Description:-" + cursor.getString(5));
            ruletxt.setText(Html.fromHtml(s));
        }
        cursor.close();

    }


    private void listener() {
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventCameraActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });
        stuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EventCameraActivity.this, EventAllDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void studentsOfEventAPI() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.studentInEvent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(EventCameraActivity.this,"Api list student link successfully",Toast.LENGTH_LONG).show();
                    parseStudentList(response);
                    Log.d("Debug", "Api list student link successfully");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(EventCameraActivity.this, "Error in Network Link", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EventCameraActivity.this, EventCoordinatorActivity.class));
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String otpprof = sharedPreferences.getString("otp", "");
                String emailprof = sharedPreferences.getString("email", "");
                String event_id = getIntent().getStringExtra("eventid");
                map.put("otp", otpprof);
                map.put("email", emailprof);
                map.put("event_id", event_id);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(EventCameraActivity.this);
        queue.add(stringRequest);
    }

    private void parseStudentList(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        if (error.equals("false")) {
            String message = jsonObject.getString("message");
            JSONArray jsonArray = new JSONArray(message);
            Log.d("DEBUG", "" + message);
            DatabaseHelper db = new DatabaseHelper(EventCameraActivity.this);

            EventStudentsTable.deleteTableData(db.getWritableDatabase(), "delete from " + EventStudentsTable.TABLE_NAME);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String stu_name = jsonObject1.getString("stu_name");
                String stu_reg_no = jsonObject1.getString("stu_reg_no");
                String stu_college = jsonObject1.getString("stu_college");
                String stu_email = jsonObject1.getString("stu_email");
                String stu_contact = jsonObject1.getString("stu_contact");
                String ev_event_att = jsonObject1.getString("ev_event_att");
                ContentValues cv = new ContentValues();
                cv.put(EventStudentsTable.Stu_name, stu_name);
                cv.put(EventStudentsTable.Stu_reg_no, stu_reg_no);
                cv.put(EventStudentsTable.Stu_college, stu_college);
                cv.put(EventStudentsTable.Stu_email, stu_email);
                cv.put(EventStudentsTable.Stu_contact, stu_contact);
                cv.put(EventStudentsTable.Ev_event_att, ev_event_att);
                Log.d("DEBUG", "" + cv.toString());
                long j = EventStudentsTable.insert(db.getWritableDatabase(), cv);
                Log.d("Debug", "" + j);
                progressDialog.cancel();
            }
        } else {
            String message = jsonObject.getString("message");
            Log.d("Debug", "" + message);
            progressDialog.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_BARCODE_CAPTURE) {
            LayoutInflater factory = LayoutInflater.from(EventCameraActivity.this);
            final View dialogview = factory.inflate(R.layout.dialog_scanner_result, null);
            final AlertDialog dialog = new AlertDialog.Builder(EventCameraActivity.this).create();
            dialog.setView(dialogview);
            TextView regno = dialogview.findViewById(R.id.nametxt);

            if (resultCode == RC_BARCODE_CAPTURE) {
                if (data != null) {
                    Log.d("Data", data.getStringExtra("barcodeValue"));
                    String barcodeValue = data.getStringExtra("barcodeValue");
                    regno.setText(barcodeValue);
                    int flag1 = 0;
                    //dialog.show();
                    DatabaseHelper db = new DatabaseHelper(EventCameraActivity.this);
                    Cursor cursor = db.getReadableDatabase().rawQuery("select * from " + EventStudentsTable.TABLE_NAME, null);
                    Log.d("Debug", "cursor y :" + cursor.toString());
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            if (cursor.getString(1).equals(barcodeValue) && cursor.getString(5).equals("0")) {
                                progressDialog.show();
                                markAttendanceAPI(barcodeValue);

                                flag1 = 1;
                                break;

                            }

                        }
                        if (flag1 == 0) {
                            Toast.makeText(EventCameraActivity.this, "Already exist or not Registered", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(EventCameraActivity.this, "Error reading in Data from db", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void markAttendanceAPI(final String barcodeValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.studentAttendanceCheck, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseStudentAttendance(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Debug", "response got");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.d("Debug", "Error in Api attendance");

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                String emailprof = sharedPreferences.getString("email", "");
                String otpprof = sharedPreferences.getString("otp", "");
                String event_id = getIntent().getStringExtra("eventid");
                map.put("email", emailprof);
                map.put("otp", otpprof);
                map.put("stu_reg_no", barcodeValue);
                map.put("event_id", event_id);
                return map;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(EventCameraActivity.this);
        queue.add(request);
    }

    private void parseStudentAttendance(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        progressDialog.cancel();
        if (error.equals("false")) {
            studentsOfEventAPI();
            Toast.makeText(EventCameraActivity.this, message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EventCameraActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}
