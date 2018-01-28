package org.poornima.aarohan.aarohan_2018forcoorfinators;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.AbsentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.AllAbsentFrag;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments.PresentFrag;

import java.util.ArrayList;
import java.util.List;
//TODO Rules And Regulation in Dialog Box
//TODO QR Scanner and show student profile on Dialog Box
//TODO Attendance List

public class EventAllDetailsActivity extends AppCompatActivity {

    //private FloatingActionButton scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all_details);
        init();
    }

    private void init() {
        //arrayList = new ArrayList<>();
        ViewPager viewpager = findViewById(R.id.viewpager);
        TabLayout tablayout = findViewById(R.id.tabs);
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
        viewpager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> getmFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager fm) {
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

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            getmFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getmFragmentTitleList.get(position);
        }
    }
}
