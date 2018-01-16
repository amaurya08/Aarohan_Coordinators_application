package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.EventStudentListAdapt;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.AbsentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.AllAbsentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.PresentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.RulesFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.EventStudentPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventStudentsTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO Rules And Regulation in Dialog Box
//TODO QR Scanner and show student profile on Dialog Box
//TODO Attendance List

public class EventAllDetailsActivity extends AppCompatActivity {


    private TabLayout tablayout;
    private ViewPager viewpager;
    private ProgressDialog progressDialog;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private FloatingActionButton scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all_details);
        init();
        listener();
    }
    private void init() {
        //arrayList = new ArrayList<>();
        viewpager = findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tabs);
        scan = findViewById(R.id.scanner);
        progressDialog = new ProgressDialog(EventAllDetailsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        studentsOfEventAPI();
        setupViewPager(viewpager);
        tablayout.setupWithViewPager(viewpager);
    }
    private void listener() {
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAllDetailsActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });
    }

    private void studentsOfEventAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.studentInEvent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseStudentList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(EventAllDetailsActivity.this, "error in api link", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EventAllDetailsActivity.this, EventCoordinatorActivity.class));
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
        RequestQueue queue = Volley.newRequestQueue(EventAllDetailsActivity.this);
        queue.add(stringRequest);
    }

    private void parseStudentList(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        if (error.equals("false")) {
            String message = jsonObject.getString("message");
            JSONArray jsonArray = new JSONArray(message);
            Log.d("DEBUG", "" + message);
            DatabaseHelper db = new DatabaseHelper(EventAllDetailsActivity.this);
            
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

    public void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle data = new Bundle();

        //data1.putSerializable("allArray", (Serializable) arrayList);
        data.putString("eventid", getIntent().getStringExtra("eventid"));
        Fragment fragmentRules = new RulesFrag();
        fragmentRules.setArguments(data);
        //Fragment fragmentAll = new AllAbsentFrag();
        //fragmentAll.setArguments(data1);
        adapter.addFragment(new PresentFrag(), "Present");
        adapter.addFragment(new AbsentFrag(), "Absent");
        adapter.addFragment(new AllAbsentFrag(), "All");
        adapter.addFragment(fragmentRules, "Rules");
        viewpager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> getmFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            getmFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getmFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_BARCODE_CAPTURE) {
            LayoutInflater factory = LayoutInflater.from(EventAllDetailsActivity.this);
            final View dialogview = factory.inflate(R.layout.dialog_scanner_result, null);
            final AlertDialog dialog = new AlertDialog.Builder(EventAllDetailsActivity.this).create();
            dialog.setView(dialogview);
            TextView regno = dialogview.findViewById(R.id.nametxt);

            if (resultCode == RC_BARCODE_CAPTURE) {
                if (data != null) {
                    Log.d("Data", data.getStringExtra("barcodeValue"));
                    String barcodeValue = data.getStringExtra("barcodeValue");
                    regno.setText(barcodeValue);
                    int flag1 = 0;
                    //dialog.show();
                    DatabaseHelper db = new DatabaseHelper(EventAllDetailsActivity.this);
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
                            Toast.makeText(EventAllDetailsActivity.this, "Already exist or not Registered", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(EventAllDetailsActivity.this, "Error reading in Data from db", Toast.LENGTH_SHORT).show();
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
                Log.d("Debug","response got");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Log.d("Debug", "Error in Api attendance");
                
            }
        })
        {
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
        RequestQueue queue = Volley.newRequestQueue(EventAllDetailsActivity.this);
        queue.add(request);
    }

    private void parseStudentAttendance(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        progressDialog.cancel();
        if (error.equals("false")) {
            studentsOfEventAPI();
            Toast.makeText(EventAllDetailsActivity.this,message,Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(EventAllDetailsActivity.this,message,Toast.LENGTH_LONG).show();
        }
    }
}
