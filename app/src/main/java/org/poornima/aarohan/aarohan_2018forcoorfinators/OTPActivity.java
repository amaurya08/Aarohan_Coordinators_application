package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private EditText otp_box;
    private Button verify_otp;
    private String intentEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
        intentEmail=getIntent().getStringExtra("email");
        sendOtp(intentEmail);
        methodListners();
    }

    private void methodListners(){
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOtp(otp_box.getText().toString(),intentEmail);
            }
        });
    }

    private void VerifyOtp(final String userOtp, final String email) {
        try{
            StringRequest stringRequest=new StringRequest(Request.Method.POST, URLHelper.verifyOTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        parseOtpVerifyResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OTPActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map=new HashMap<>();
                    map.put("email",email);
                    map.put("otp",userOtp);
                    map.put("type","COORDINATOR");
                    return map;
                }
            };
            RequestQueue queue= Volley.newRequestQueue(OTPActivity.this);
            queue.add(stringRequest);
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseOtpVerifyResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        if (error.equals("false")) {
            makeSession(message);
            if ((getIntent().getStringExtra("modulename").toString()).equals("module1")){
                Intent intent = new Intent(OTPActivity.this,EventCoordinatorActivity.class);
                startActivity(intent);
                finish();
            }
            else if ((getIntent().getStringExtra("modulename").toString()).equals("module2")){
                Intent intent = new Intent(OTPActivity.this,EventCoordinatorActivity.class);
                startActivity(intent);
                finish();
            }
           else if ((getIntent().getStringExtra("modulename").toString()).equals("module3")){
                Intent intent = new Intent(OTPActivity.this,EventCoordinatorActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Log.d("DEBUG","module error");
            }
        } else
            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void makeSession(String message) {
        SharedPreferences sharedPref = getSharedPreferences("aarohan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", intentEmail);
        editor.putString("otp", otp_box.getText().toString());
        editor.putString("sid", message);
        editor.putBoolean("is", true);
        Log.d(TAG, "Making Session with " + intentEmail + " " + otp_box.getText().toString() + " " + message);
        editor.apply();
    }

    private void sendOtp(final String email) {
        try{
            StringRequest stringRequest=new StringRequest(Request.Method.POST, URLHelper.sendOTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ParseOtpResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OTPActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map=new HashMap<>();
                    map.put("email",email);
                    map.put("type","COORDINATOR");
                    return map;
                }
            };
            RequestQueue queue= Volley.newRequestQueue(OTPActivity.this);
            queue.add(stringRequest);
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ParseOtpResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String error = jsonObject.getString("error");
            String message = jsonObject.getString("message");
            if (error.equals("false")) {
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Email ID is not Registered. Please Contact Web / Aplication Developer", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        otp_box=findViewById(R.id.otpbox);
        verify_otp=findViewById(R.id.verifyotp);
    }
}
