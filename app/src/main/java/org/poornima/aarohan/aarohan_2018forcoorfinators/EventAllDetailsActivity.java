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
    private FloatingActionButton scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all_details);
        init();
    }
    private void init() {
        //arrayList = new ArrayList<>();
        viewpager = findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tabs);
        //scan = findViewById(R.id.scanner);

        //studentsOfEventAPI();
        setupViewPager(viewpager);
        tablayout.setupWithViewPager(viewpager);
    }

    public void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PresentFrag(), "Present");
        adapter.addFragment(new AbsentFrag(), "Absent");
        adapter.addFragment(new AllAbsentFrag(), "All");
        //adapter.addFragment(new RulesFrag(), "Rules");
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
}
