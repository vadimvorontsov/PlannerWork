package ru.anroidapp.plannerwork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import ru.anroidapp.plannerwork.ScreenSlide.ScreenSlidePageFragment1;
import ru.anroidapp.plannerwork.ScreenSlide.ScreenSlidePageFragment2;
import ru.anroidapp.plannerwork.ScreenSlide.ScreenSlidePageFragment3;
import ru.anroidapp.plannerwork.ScreenSlide.ScreenSlidePageFragment4;
import ru.anroidapp.plannerwork.ScreenSlide.ScreenSlidePageFragment5;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    Toolbar toolbar;
    Context cntxt;
    TextView textView;
    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cntxt = this;

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
        initializeNavigationDrawer(toolbar);

        mPager = (ViewPager) findViewById(R.id.pagerMain);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


    }

    @Override
    public void onBackPressed(){
        if(mPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void initializeNavigationDrawer(Toolbar toolbar) {

      //  IProfile profile = new ProfileDrawerItem()
      //          .withName("VadArt")
      //          .withEmail("PlannerWork@gmail.com");

        AccountHeader resultHeder = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.arni2)
              //  .addProfiles(profile)
                .build();

        Drawer resultDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withAccountHeader(resultHeder)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        //new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navDraw_1)
                                .withIcon(R.mipmap.home),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navDraw_2)
                                .withIcon(R.mipmap.ic_calendare),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navDraw_3)
                                .withIcon(R.mipmap.arhiv_2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navDraw_4)
                                .withIcon(R.mipmap.settings_1),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.navDraw_5)
                                .withIcon(R.mipmap.information_1),
                        new DividerDrawerItem()


                )
                        //            .withSliderBackgroundColor(R.color.md_grey_100)
                .build();

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm2) {
            super(fm2);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ScreenSlidePageFragment1();
            }
            if (position == 1) {
                return new ScreenSlidePageFragment2();
            }
            if (position == 2) {
                return new ScreenSlidePageFragment3();
            }
            if (position == 3) {
                return new ScreenSlidePageFragment4();
            }
            if (position == 4) {
                return new ScreenSlidePageFragment5();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }




        public void onBtn1Click(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void onBtn2Click(View view) {
        startActivity(new Intent(this, CalendarActivity.class));
    }

    public void onBtn3Click(View view) {
        startActivity(new Intent(this, ProcedureActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
