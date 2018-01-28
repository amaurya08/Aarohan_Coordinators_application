package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
                finish();
            }
        },3000);
    }


    private void startApp() {
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan",MODE_PRIVATE);
        String type = sharedPreferences.getString("type","");
        boolean is = sharedPreferences.getBoolean("is",false);

        if(checkSession()){
            Log.d("DEBUG",type+""+is);
            switch (type)
            {
                case "EVENT_COOR":
                    Intent intent = new Intent(SplashActivity.this, EventCoordinatorActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case "SECURITY":
                    Intent intent1 = new Intent(SplashActivity.this, RegistrationActivity.class);
                    startActivity(intent1);
                    finish();
                    break;

                case "HOSPITALITY":
                    Intent intent2 = new Intent(SplashActivity.this, AccomodationActivity.class);
                    startActivity(intent2);
                    finish();
                    break;

                default:
                    Toast.makeText(SplashActivity.this, "NOT Forwarded", Toast.LENGTH_SHORT).show();
                    break;

            }
            }
        else {
            startActivity(new Intent(SplashActivity.this,PromptLoginActivity.class));
        }

    }

    private boolean checkSession()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("aarohan",MODE_PRIVATE);
        return sharedPreferences.getBoolean("is", false);

    }

}
