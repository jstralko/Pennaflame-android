package com.pennaflame.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MetricActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    private MetricFragment englishFragment;
    private MetricFragment metricFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metric_layout);

        mActionBar = getSupportActionBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navDrawer);
        mDrawerList.setAdapter(new PFArrayAdapter(this, R.layout.drawer_list_item));
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                Fragment fragment;
                if (tab.getText().equals("English")) {
                    if (englishFragment == null) {
                        englishFragment = MetricFragment.newInstance(MetricFragment.ENGLISH_TYPE);
                    }
                    fragment = englishFragment;
                } else {
                    if (metricFragment == null) {
                        metricFragment = MetricFragment.newInstance(MetricFragment.METRIC_TYPE);
                    }
                    fragment = metricFragment;
                }
                //switch to the new fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        mActionBar.addTab(mActionBar.newTab()
                .setText("English")
                .setTabListener(tabListener));

        mActionBar.addTab(mActionBar.newTab()
                .setText("Metric")
                .setTabListener(tabListener));

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.metric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PFArrayAdapter extends ArrayAdapter<String> {
        public PFArrayAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater li = (LayoutInflater) MetricActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = li.inflate(R.layout.drawer_list_item, null);
            } else {
                row = convertView;
            }

            TextView tv = (TextView) row.findViewById(R.id.list_item_text);
            switch(position) {
                case 0:
                    tv.setText("Home");
                    break;
                case 1:
                    tv.setText("English/Metric Converter");
                    break;
                case 2:
                    tv.setText("Fraction/Decimal Convert");
                    break;
                case 3:
                    tv.setText("Hardness Case Depth");
                    break;
                case 4:
                    tv.setText("MTI Statement");
                    break;
                case 5:
                    tv.setText("Hardness Chart");
                    break;
                case 6:
                    tv.setText("Contact Us");
                    break;
                default:
            }
            return row;
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}
