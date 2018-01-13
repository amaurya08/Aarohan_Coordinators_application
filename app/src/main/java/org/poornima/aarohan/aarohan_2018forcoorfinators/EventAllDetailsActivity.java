package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.AbsentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.AllAbsentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.PresentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.RulesFrag;

import java.util.ArrayList;
import java.util.List;
//TODO Rules And Regulation in Dialog Box
//TODO QR Scanner and show student profile on Dialog Box
//TODO Attendance List

public class EventAllDetailsActivity extends AppCompatActivity {


    private TabLayout tablayout;
    private ViewPager viewpager;
    private static final int RC_BARCODE_CAPTURE = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all_details);



        viewpager = findViewById(R.id.viewpager);
        setupViewPager(viewpager);

        tablayout = findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpager);

        FloatingActionButton scan = findViewById(R.id.scanner);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAllDetailsActivity.this,BarcodeCaptureActivity.class);
                startActivityForResult(intent,RC_BARCODE_CAPTURE);
            }
        });


    }

    public void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter =new ViewPagerAdapter(getSupportFragmentManager());
        Bundle data=new Bundle();
        data.putString("eventname",getIntent().getStringExtra("eventname"));
        Fragment fragmentRules=new RulesFrag();
        fragmentRules.setArguments(data);
        adapter.addFragment(new PresentFrag(),"Present");
        adapter.addFragment(new AbsentFrag(),"Absent");
        adapter.addFragment(new AllAbsentFrag(),"All");
        adapter.addFragment(fragmentRules,"Rules");
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
        public void addFragment(Fragment fragment,String title)
        {
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


        if (requestCode==RC_BARCODE_CAPTURE)
        {

            LayoutInflater factory = LayoutInflater.from(EventAllDetailsActivity.this);
            final View dialogview= factory.inflate(R.layout.dialog_scanner_result,null);
            final AlertDialog dialog = new AlertDialog.Builder(EventAllDetailsActivity.this).create();
            dialog.setView(dialogview);
            TextView regno = dialogview.findViewById(R.id.nametxt);
            if(resultCode== RC_BARCODE_CAPTURE)
            {
                if(data!=null)
                {
                    Log.d("Data", data.getStringExtra("barcodeValue"));
                    String barcodeValue = data.getStringExtra("barcodeValue");
                    regno.setText(barcodeValue);
                    dialog.show();

                }
            }

        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
