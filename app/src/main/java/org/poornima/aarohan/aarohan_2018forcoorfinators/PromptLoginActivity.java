package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PromptLoginActivity extends AppCompatActivity {

    private Button login;
    private EditText emailtxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_login);
        init();
        listeners();


    }

    private void listeners() {
    }

    private void init() {
        
    }
}
