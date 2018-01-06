package org.poornima.aarohan.aarohan_2018forcoorfinators;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all_details);



        viewpager = findViewById(R.id.viewpager);
        setupViewPager(viewpager);

        tablayout = findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpager);


    }

    public void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter =new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PresentFrag(),"Present");
        adapter.addFragment(new AbsentFrag(),"Absent");
        adapter.addFragment(new AllAbsentFrag(),"All");
        adapter.addFragment(new RulesFrag(),"Rules");
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
}
