package com.ssrij.warnindia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;
import com.greysonparrelli.permiso.Permiso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {

    ArrayList<EarthquakeObject> quakeDataList;
    ArrayList<TsunamiObject> tsunamiDataList;
    ArrayList<String> cycloneDataList;
    ArrayList<CycloneObject> detailedCycloneDataList;
    SheetLayout mSheetLayout;
    CoordinatorLayout coordinatorLayout;
    Boolean isFirstRun;
    SharedPreferences prefs;
    Boolean locPermGranted;
    CustomTabs.Warmer warmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Permiso.getInstance().setActivity(this);
        setContentView(R.layout.activity_main);
        warmer = CustomTabs.with(getApplicationContext()).warm();
        quakeDataList = getIntent().getParcelableArrayListExtra("quakeDataList");
        tsunamiDataList = getIntent().getParcelableArrayListExtra("tsunamiDataList");
        cycloneDataList = getIntent().getStringArrayListExtra("cycloneDataList");
        detailedCycloneDataList = getIntent().getParcelableArrayListExtra("detailedCycloneDataList");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        isFirstRun = prefs.getBoolean("isFirstRun", true);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_layout);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Earthquake"));
        tabLayout.addTab(tabLayout.newTab().setText("Tsunami"));
        tabLayout.addTab(tabLayout.newTab().setText("Cyclone"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        mSheetLayout.setFab(fab);
        mSheetLayout.setFabAnimationEndListener(MainActivity.this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                    @Override
                    public void onPermissionResult(Permiso.ResultSet resultSet) {
                        if (resultSet.areAllPermissionsGranted()) {
                            locPermGranted = true;
                            mSheetLayout.expandFab();

                            final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                            appBarLayout.animate()
                                    .alpha(0.0f)
                                    .setDuration(250);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    appBarLayout.setVisibility(View.GONE);
                                }
                            },250);
                        } else {
                            locPermGranted = false;
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout,
                                            "Permission to access location denied",
                                            Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                        Permiso.getInstance().showRationaleInDialog("Location access required",
                                "Access to your device's location is required in order to see crowdsourced " +
                                        "reports on map and in order to publish your own report",
                                null, callback);
                    }
                }, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);

            }
        });

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), quakeDataList, tsunamiDataList,
                        cycloneDataList, detailedCycloneDataList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fab.show();
                        break;

                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (isFirstRun) {
            showFirstRunWarning();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFabAnimationEnd() {
        if (locPermGranted) {
            startActivityForResult(new Intent(getApplicationContext(), CrowdsourcedQuakeReports.class), 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mSheetLayout.contractFab();
            final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
            appBarLayout.animate()
                    .alpha(1.0f)
                    .setDuration(250);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    appBarLayout.setVisibility(View.VISIBLE);
                }
            },250);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout,
                                "Not yet implemented",
                                Snackbar.LENGTH_SHORT);
                snackbar.show();
                return true;
            case R.id.help_menu_item:
                Snackbar snackbar1 = Snackbar
                        .make(coordinatorLayout,
                                "Not yet implemented",
                                Snackbar.LENGTH_SHORT);
                snackbar1.show();
                return true;
            case R.id.about_menu_item:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFirstRunWarning() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Beta Warning");
        builder.setCancelable(false);
        builder.setMessage("THIS APP IS IN BETA\nSome features may or may not work correctly or are under development\n\nThis app uses complex processing to extract data from various governmental websites " +
                "and thus can stop working or show incorrect data without warning in case the governmental websites " +
                "undergo structural changes, although efforts will be made to make sure data available is always correct");
        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                prefs.edit().putBoolean("isFirstRun", false).apply();
            }
        });
        builder.show();
    }
}
