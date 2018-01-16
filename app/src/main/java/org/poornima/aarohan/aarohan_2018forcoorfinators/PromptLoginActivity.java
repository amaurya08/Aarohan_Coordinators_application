package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class PromptLoginActivity extends AppCompatActivity {

    private Button Submit_email;
    private EditText Email_id;
    private ProgressDialog progressDialog;
    private Animation animation;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_login);
        init();
        methodListeners();
    }

    private void init() {
        fragmentManager = PromptLoginActivity.this.getSupportFragmentManager();
        progressDialog = new ProgressDialog(PromptLoginActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Verifying Email...");
        Email_id = findViewById(R.id.email);
        Submit_email = findViewById(R.id.submit_email);
        animation = AnimationUtils.loadAnimation(PromptLoginActivity.this,
                R.anim.rightexit);

    }

    private void methodListeners() {
        Submit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                verifyEmail(Email_id.getText().toString());
                overridePendingTransition(R.anim.leftenter, R.anim.rightexit);
            }
        });
    }

    private void verifyEmail(String em) {
        final String email=em;
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.verifyEmail, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.cancel();
                    verifyAndParseResponse(response, email);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    Log.d("TAG", error + "");
                    Toast.makeText(PromptLoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue queue = Volley.newRequestQueue(PromptLoginActivity.this);
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void verifyAndParseResponse(String response, String email) {
        Log.d("DEBUG", "I am here ");
        try {
            JSONObject jsonObject = new JSONObject(response);
            String error = jsonObject.getString("error");
            if (error.equals("false")) {
                Log.d("DEBUG", "now I am here ");
                Intent intent = new Intent(PromptLoginActivity.this, OTPActivity.class);
                intent.putExtra("email", email);
                //intent.putExtra("modulename", getIntent().getStringExtra("modulename").toString());
                startActivity(intent);
                finish();
            } else {
                Log.d("DEBUG", "Error in parsing");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
