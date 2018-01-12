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
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO Profile Activity
//TODO  Events Name in List View
//TODO Event Listenenr invoke EvenAllDetailsActivit
//TODO if event id in null find any func.
public class EventCoordinatorActivity extends AppCompatActivity {
    private ListView evelist;
    private TextView nametext;
    private ProgressDialog progressDialog;
    private ArrayList<String> arrayList;
    private Button logoutbut;
    private ListAdapter myadapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_coordinator);
        init();
        progressDialog.show();
        eventOfCoordinator();
        arrayList=new ArrayList<>();
        myadapter = new ArrayAdapter<String>(EventCoordinatorActivity.this,android.R.layout.simple_list_item_1,arrayList);
        fetchCoordinatorDetails();
        evelist.setAdapter(myadapter);


        progressDialog.cancel();
        methodListener();

    }



    private void fetchCoordinatorDetails() {

        DatabaseHelper db = new DatabaseHelper(EventCoordinatorActivity.this);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from "+ EventCoordinatorDetailsTable.TABLE_NAME,null);
        Log.d("Debug","cursor :"+cursor.toString());
        //nametext.setText(EventCoordinatorDetailsTable.Co_name);

        while (cursor.moveToNext())
        {
            Log.d("Debug",cursor.getString(1));
            arrayList.add(cursor.getString(1));
            nametext.setText(cursor.getString(9));
        }
        cursor.close();
    }

    private void methodListener() {
        evelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EventCoordinatorActivity.this,EventAllDetailsActivity.class);
                startActivity(intent);
            }
        });
        logoutbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checksession())
                {
                    logoutAPI();
                    SharedPreferences sharedPreferences = getSharedPreferences("aarohan",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email","");
                    editor.putString("otp","");
                    editor.putBoolean("is",false);
                    editor.putString("type","");
                    editor.apply();
                    DatabaseHelper db = new DatabaseHelper(EventCoordinatorActivity.this);
                    EventCoordinatorDetailsTable.clearCoordinatorDetail(db.getWritableDatabase(), "delete from " + EventCoordinatorDetailsTable.TABLE_NAME);
                    Toast.makeText(EventCoordinatorActivity.this,"LogoutSuccessfull",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EventCoordinatorActivity.this,PromptLoginActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent(EventCoordinatorActivity.this,PromptLoginActivity.class);
                    startActivity(intent);
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

    }

    private void init() {
        evelist = findViewById(R.id.eventlist);
        nametext = findViewById(R.id.nametxt);
        logoutbut=findViewById(R.id.logoutbut);
        progressDialog = new ProgressDialog(EventCoordinatorActivity.this);
        progressDialog.setMessage("Loading Profile...");
        progressDialog.setCancelable(false);

    }
    private Boolean checksession()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("is",false))
            return true;
        else
            return false;
    }
    private void eventOfCoordinator() {
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
                String event_name = jsonObject1.getString("event_name");
                String event_detail = jsonObject1.getString("event_detail");
                String event_time = jsonObject1.getString("event_time");
                String event_location = jsonObject1.getString("event_location");
                String event_date = jsonObject1.getString("event_date");
                ContentValues cv = new ContentValues();
                cv.put(EventCoordinatorDetailsTable.Event_id, event_id);
                cv.put(EventCoordinatorDetailsTable.Co_name, co_name);
                cv.put(EventCoordinatorDetailsTable.Event_date, event_date);
                cv.put(EventCoordinatorDetailsTable.Event_time, event_time);
                cv.put(EventCoordinatorDetailsTable.Event_location, event_location);
                cv.put(EventCoordinatorDetailsTable.Event_detail, event_detail);
                cv.put(EventCoordinatorDetailsTable.Event_name, event_name);
                long jaggu = EventCoordinatorDetailsTable.insert(db.getWritableDatabase(), cv);
                Log.d("Debug", "" + jaggu);
            }


        }
        else
        {
            setContentView(R.layout.no_event_found);
            TextView noevent = findViewById(R.id.noeventtxt);
            noevent.setText("You Have No event Registered");
        }

    }

}
