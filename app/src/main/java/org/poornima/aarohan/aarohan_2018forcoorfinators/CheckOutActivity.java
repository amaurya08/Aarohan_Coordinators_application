package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.AccommodationStudentListAdapt;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.AccommodationStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.AccomodationStudentTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int RC_BARCODE_CAPTURE1 = 9002;
    private static final String TAG = "DEBUG";
    private Button cam;
    private ListView listcheck;
    private ProgressDialog progressDialog;
    private ArrayList<AccommodationStudentPojo> arrayList;
    private AccommodationStudentListAdapt myadapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        init();
        AccomodationListApi();
        listofstuCheckin();
        methodListener();
    }

    private void listofstuCheckin() {
    }

    private void init() {
        arrayList = new ArrayList<>();
        listcheck = findViewById(R.id.listcheck);
        myadapter = new AccommodationStudentListAdapt(CheckOutActivity.this, arrayList);
        listcheck.setAdapter(myadapter);
        cam = findViewById(R.id.checkOutScan);
        progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please wait...");
    }

    void methodListener() {
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOutActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE1);
            }
        });
        listcheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String regno1 = arrayList.get(i).getStu_reg_no();
                DatabaseHelper db = new DatabaseHelper(CheckOutActivity.this);

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
                                ((TextView) dialogview.findViewById(R.id.paymenttxt)).setText("Payment Status:-No");
                            } else {
                                ((TextView) dialogview.findViewById(R.id.paymenttxt)).setText("Payment Status:-Yes");
                            }
                            AlertDialog.Builder dialogbuild = new AlertDialog.Builder(CheckOutActivity.this);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE1) {
            if (resultCode == RC_BARCODE_CAPTURE) {
                if (data != null) {
                    int flag = 1;

                    final String barcodeValue = data.getStringExtra("barcodeValue");
                    Log.d("DEBUG", "Data Scanned" + barcodeValue);
                    DatabaseHelper db = new DatabaseHelper(CheckOutActivity.this);
                    Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + AccomodationStudentTable.TABLE_NAME, null);
                    while (cursor.moveToNext()) {
                        if (cursor.getString(1).equals(barcodeValue)) {
                            flag = 0;
                            Log.d("check12", cursor.getString(5));
                            if (!(cursor.getString(4).equals("null"))) {
                                if (cursor.getString(5).equals("null")) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    final String timestamp = sdf.format(new Date());
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
                                            ((TextView) dialogview.findViewById(R.id.paymenttxt)).setText("Payment Status:-No");
                                        } else {
                                            ((TextView) dialogview.findViewById(R.id.paymenttxt)).setText("Payment Status:-Yes");
                                        }
                                        final AlertDialog.Builder dialogbuild = new AlertDialog.Builder(this);
                                        dialogbuild.setView(dialogview)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        progressDialog.show();
                                                        checkinDetail(barcodeValue, timestamp);
                                                        dialogInterface.cancel();

                                                    }
                                                });
                                        dialogbuild.setView(dialogview)
                                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        Toast.makeText(CheckOutActivity.this, "Data not Saved", Toast.LENGTH_SHORT).show();
                                                        dialogInterface.cancel();

                                                    }
                                                });
                                        dialogbuild.show().create();
                                        dialogbuild.setCancelable(false);
                                        break;
                                    }
                                } else {
                                    Toast.makeText(CheckOutActivity.this, " Already Checked Out", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(CheckOutActivity.this, "You have not Checked In Yet", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                    cursor.close();
                    if (flag == 1) {
                        Toast.makeText(CheckOutActivity.this, "Registration Not Done", Toast.LENGTH_LONG).show();
                    }


                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void checkinDetail(String barcodeValue, String timestamp) {
        final String timeStamp = timestamp;
        final String value = barcodeValue;
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.accommodationAttendance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //progressDialog.cancel();
                    Log.d("DEBUG", "Response Recieved\n" + response);
                    parseCheckinDetail(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(CheckOutActivity.this, "Error in Network Link", Toast.LENGTH_LONG).show();
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
                map.put("stu_reg_no", value);
                map.put("time_stamp", timeStamp);
                map.put("check_type", "Check Out");
                Log.d("DEBUG", map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(CheckOutActivity.this);
        queue.add(request);
    }

    void parseCheckinDetail(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            Log.d("DEBUG", "" + message);
            AccomodationListApi();


        } else {
            progressDialog.cancel();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void AccomodationListApi() {
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.AccommodationList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseAccoStudentList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Success in Api Link");
                //Toast.makeText(CheckOutActivity.this, "Success in Api Link", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.d(TAG, "Error in Network Link");
                Toast.makeText(CheckOutActivity.this, "Error in Network Link", Toast.LENGTH_LONG).show();

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
        RequestQueue queue = Volley.newRequestQueue(CheckOutActivity.this);
        queue.add(request);
    }

    private void parseAccoStudentList(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            JSONArray studentArray = new JSONArray(message);
            Log.d(TAG, "" + message);
            DatabaseHelper db = new DatabaseHelper(CheckOutActivity.this);
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
                //Toast.makeText(CheckOutActivity.this, "Database contain data", Toast.LENGTH_LONG).show();
                if (AccomodationStudentTable.insert(db.getWritableDatabase(), cv) > 0)
                    Log.d(TAG, "Databse insert");
                else
                    Log.d(TAG, "Databse Insertion Error");
            }
            SetDataOnList();
            progressDialog.cancel();


        } else {
            progressDialog.cancel();
            Toast.makeText(CheckOutActivity.this, message, Toast.LENGTH_LONG).show();

        }
    }

    private void SetDataOnList() {
        arrayList.clear();
        DatabaseHelper db = new DatabaseHelper(CheckOutActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + AccomodationStudentTable.TABLE_NAME + " WHERE " + AccomodationStudentTable.Rc_check_out + " IS NOT " + "\"null\"", null);
        Log.d(TAG, "Reading DAtabase" + cursor.getCount());
        //Toast.makeText(CheckOutActivity.this, "display done", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Cursor is not null");

        while (cursor.moveToNext()) {
            arrayList.add(new AccommodationStudentPojo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        }

        myadapter.notifyDataSetChanged();
        cursor.close();
    }
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(CheckOutActivity.this,AccomodationActivity.class));
        finish();
    }

}
