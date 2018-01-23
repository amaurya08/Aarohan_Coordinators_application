package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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
import org.poornima.aarohan.aarohan_2018forcoorfinators.Adapter.EventCoordinatorListAdapter;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo.CoordinatorDataPojo;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO UI designing of all event and Accomodation activity.
//TODO remove progressDialog
//TODO include the network class
//TODO if event id in null find any func.

public class EventCoordinatorActivity extends AppCompatActivity {
    private ListView evelist;
    private TextView nametext;
    private ProgressDialog progressDialog;
    private ArrayList<CoordinatorDataPojo> arrayList;
    private Button logoutbut;
    EventCoordinatorListAdapter myadapter;
    //private ArrayAdapter<String> myadapter;
    TextView initaltxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_coordinator);
        init();
        eventOfCoordinator();
        //progressDialog.show();
        methodListener();
    }

    private void init() {
        evelist = findViewById(R.id.eventlist);
        nametext = findViewById(R.id.nametxt);
        logoutbut = findViewById(R.id.logoutbut);

        progressDialog = new ProgressDialog(EventCoordinatorActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        initaltxt = findViewById(R.id.inittxt);
        nametext.setText("");
        initaltxt.setText("");
        arrayList = new ArrayList<>();
        myadapter = new EventCoordinatorListAdapter(EventCoordinatorActivity.this, arrayList);
        evelist.setAdapter(myadapter);


    }



    private void fetchCoordinatorDetails() {


        DatabaseHelper db = new DatabaseHelper(EventCoordinatorActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from " + EventCoordinatorDetailsTable.TABLE_NAME, null);
        Log.d("Debug", "cursor :" + cursor.toString());
        //nametext.setText(EventCoordinatorDetailsTable.Co_name);

        while (cursor.moveToNext()) {
            Log.d("Debug", cursor.getString(1));
            arrayList.add(new CoordinatorDataPojo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11)));
            nametext.setText(cursor.getString(9));
            String j = cursor.getString(9).substring(0, 1).toUpperCase() + "";
            initaltxt.setText(j);

        }
        myadapter.notifyDataSetChanged();
        progressDialog.cancel();
        cursor.close();
    }

    private void methodListener() {
        evelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CoordinatorDataPojo pojo = (CoordinatorDataPojo) arrayList.get(i);
                Intent intent = new Intent(EventCoordinatorActivity.this, EventCameraActivity.class);
                intent.putExtra("eventid", pojo.getEvent_id());
                startActivity(intent);
            }
        });
        logoutbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checksession()) {
                    logoutAPI();
                    SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", "");
                    editor.putString("otp", "");
                    editor.putString("cid","");
                    editor.putBoolean("is", false);
                    editor.putString("type", "");
                    editor.apply();
                    DatabaseHelper db = new DatabaseHelper(EventCoordinatorActivity.this);
                    EventCoordinatorDetailsTable.clearCoordinatorDetail(db.getWritableDatabase(), "delete from " + EventCoordinatorDetailsTable.TABLE_NAME);
                    Toast.makeText(EventCoordinatorActivity.this, "LogoutSuccessfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EventCoordinatorActivity.this, PromptLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(EventCoordinatorActivity.this, PromptLoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
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
        RequestQueue queue = Volley.newRequestQueue(EventCoordinatorActivity.this);
        queue.add(request);

    }


    private Boolean checksession() {
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("is", false))
            return true;
        else
            return false;
    }
    private void eventOfCoordinator() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.eventOfCoordinator, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.cancel();
                    parseCoordinatorDetail(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                fetchCoordinatorDetails();
                Toast.makeText(EventCoordinatorActivity.this, "Error in loading details of coordinator", Toast.LENGTH_LONG).show();
            }
        }) {
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
        RequestQueue queue = Volley.newRequestQueue(EventCoordinatorActivity.this);
        queue.add(request);
    }

    private void parseCoordinatorDetail(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        if (error.equals("false")) {
            String message = jsonObject.getString("message");
            JSONArray jsonArray = new JSONArray(message);

            DatabaseHelper db = new DatabaseHelper(EventCoordinatorActivity.this);
            EventCoordinatorDetailsTable.deleteTableData(db.getWritableDatabase(), "delete from " + EventCoordinatorDetailsTable.TABLE_NAME);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String co_name = jsonObject1.getString("co_name");
                String event_id = jsonObject1.getString("event_id");
                String event_participation_category = jsonObject1.getString("event_participation_category");
                String event_type = jsonObject1.getString("event_type");
                String event_category = jsonObject1.getString("event_category");
                String event_name = jsonObject1.getString("event_name");
                String event_detail = jsonObject1.getString("event_detail");
                String event_time = jsonObject1.getString("event_time");
                String event_location = jsonObject1.getString("event_location");
                String event_date = jsonObject1.getString("event_date");
                ContentValues cv = new ContentValues();
                cv.put(EventCoordinatorDetailsTable.Event_id, event_id);
                cv.put(EventCoordinatorDetailsTable.Event_participation_category, event_participation_category);
                cv.put(EventCoordinatorDetailsTable.Event_type, event_type);
                cv.put(EventCoordinatorDetailsTable.Co_name, co_name);
                cv.put(EventCoordinatorDetailsTable.Event_category, event_category);
                cv.put(EventCoordinatorDetailsTable.Event_date, event_date);
                cv.put(EventCoordinatorDetailsTable.Event_time, event_time);
                cv.put(EventCoordinatorDetailsTable.Event_location, event_location);
                cv.put(EventCoordinatorDetailsTable.Event_detail, event_detail);
                cv.put(EventCoordinatorDetailsTable.Event_name, event_name);
                long jaggu = EventCoordinatorDetailsTable.insert(db.getWritableDatabase(), cv);
                Log.d("Debug", "" + jaggu);

            }
            fetchCoordinatorDetails();
            //fetchCoordinatorDetails();
            //progressDialog.cancel();

        } else {
            Toast.makeText(EventCoordinatorActivity.this,"No Data Received",Toast.LENGTH_SHORT).show();
            //progressDialog.cancel();
        }

    }


}
