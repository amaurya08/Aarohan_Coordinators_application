package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass.URLHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler.DatabaseHelper;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private EditText otp_box;
    private Button verify_otp;
    private String intentEmail;
    private ProgressDialog progressDialog;
    private TextView resend, countdownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
        intentEmail = getIntent().getStringExtra("email");
        sendOtp(intentEmail);
        methodListners();
    }

    private void methodListners() {
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                VerifyOtp(otp_box.getText().toString(), intentEmail);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendotp();
            }
        });
    }

    private void resendotp() {
        resend.setVisibility(View.INVISIBLE);
        countdownTextView.setVisibility(View.VISIBLE);
        sendOtp(intentEmail);
        countdown();
    }

    private void countdown() {
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                String setText = "Retry after: " + millisUntilFinished / 1000 + "seconds";
                countdownTextView.setText(setText);
            }

            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                countdownTextView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void VerifyOtp(final String userOtp, final String email) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.verifyOTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressDialog.cancel();
                        parseOtpVerifyResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    Toast.makeText(OTPActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("otp", userOtp);
                    map.put("type", "COORDINATOR");
                    return map;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(OTPActivity.this);
            queue.add(stringRequest);
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseOtpVerifyResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String error = jsonObject.getString("error");
        String message = jsonObject.getString("message");
        JSONArray jsonArray = new JSONArray(message);
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        String co_id = jsonObject1.getString("co_id");
        String co_type = jsonObject1.getString("co_type");
        if (error.equals("false")) {
            if (makeSession(co_id, co_type))
                switch (co_type) {
                    case "EVENT_COOR":
                        //eventOfCoordinator();
                        Intent intent = new Intent(OTPActivity.this, EventCoordinatorActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case "SECURITY":
                        Intent intent1 = new Intent(OTPActivity.this, RegistrationActivity.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case "HOSPITALITY":
                        Intent intent2 = new Intent(OTPActivity.this, AccomodationActivity.class);
                        startActivity(intent2);
                        finish();
                        break;

                    default:
                        Toast.makeText(OTPActivity.this, "NOT Forwarded", Toast.LENGTH_SHORT).show();
                        break;
                }
        } else
            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private boolean makeSession(String c_id, String c_type) {
        SharedPreferences sharedPref = getSharedPreferences("aarohan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", intentEmail);
        editor.putString("otp", otp_box.getText().toString());
        editor.putString("type", c_type);
        editor.putString("cid", c_id);
        editor.putBoolean("is", true);
        Log.d(TAG, "Making Session with " + intentEmail + " " + otp_box.getText().toString() + " " + c_type);
        editor.apply();
        return true;
    }

    private void sendOtp(final String email) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.sendOTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ParseOtpResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OTPActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("type", "COORDINATOR");
                    return map;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(OTPActivity.this);
            queue.add(stringRequest);
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ParseOtpResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String error = jsonObject.getString("error");
            String message = jsonObject.getString("message");
            if (error.equals("false")) {
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Email ID is not Registered. Please Contact Web / Application Developer", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        otp_box = findViewById(R.id.otpbox);
        verify_otp = findViewById(R.id.verifyotp);
        progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Verifying OTP...");
        resend = findViewById(R.id.resend);
        countdownTextView = findViewById(R.id.countdownTimer);

    }


}
