package ru.anroidapp.plannerwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.anroidapp.plannerwork.contact_choose.ContactTab1;
import ru.anroidapp.plannerwork.date_choose.DateTab2;
import ru.anroidapp.plannerwork.procedure_choose.ProcedureTab3;


public class RecordActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    RecViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence titles[] = {"Контакт", "Дата", "Процедура"};
    int numbOfTabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        toolbar = (Toolbar) findViewById(R.id.tool_record);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.rec_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new RecViewPagerAdapter(getSupportFragmentManager(), titles, numbOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager_record);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs_record);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class RecViewPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence Titles[]; // This will Store the titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public RecViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                ContactTab1 tab1 = new ContactTab1();
                return tab1;
            }
            if (position == 1) {
                DateTab2 tab2 = new DateTab2();
                return tab2;
            }
            if (position == 2) {
                ProcedureTab3 tab3 = new ProcedureTab3();
                return tab3;
            }
            return null;
        }

        // This method return the titles for the Tabs in the Tab Strip
        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip
        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }

}