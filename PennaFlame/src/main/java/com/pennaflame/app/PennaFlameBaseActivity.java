package com.pennaflame.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by stralko on 10/23/13.
 */
public class PennaFlameBaseActivity extends ActionBarActivity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    private String[] mSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_penna_flame_main);

        mSections = getResources().getStringArray(R.array.app_sections);

        mActionBar = getSupportActionBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navDrawer);
        mDrawerList.setAdapter(new PFArrayAdapter(this, R.layout.drawer_list_item));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = null;
                    switch (position) {
                        case 0:
                            intent  = new Intent(PennaFlameBaseActivity.this, HomeActivity.class);
                         break;
                        case 1:
                            intent = new Intent(PennaFlameBaseActivity.this, MetricActivity.class);
                            break;
                        case 2:
                            //intent = new Intent(PennaFlameBaseActivity.this, )
                            break;
                        case 3:
                            intent = new Intent(PennaFlameBaseActivity.this, HardnessChartPickActivity.class);
                            intent.putExtra(HomeActivity.ROW_TITLE_ID, R.array.case_depth_row_header_titles);
                            intent.putExtra(HomeActivity.KEYS_ID, R.array.case_depth_keys);
                            intent.putExtra(HomeActivity.CHART_TITLE_ID, R.string.title_activity_hardness_case_depth);
                            break;
                        case 4:
                            intent = new Intent(PennaFlameBaseActivity.this, MTIStatementActivity.class);
                            break;
                        case 5:
                            intent = new Intent(PennaFlameBaseActivity.this, HardnessChartPickActivity.class);
                            intent.putExtra(HomeActivity.ROW_TITLE_ID, R.array.row_header_titles);
                            intent.putExtra(HomeActivity.KEYS_ID, R.array.keys);
                            intent.putExtra(HomeActivity.CHART_TITLE_ID, R.string.title_activity_hardness_chart);
                            break;
                        case 6:
                            intent = new Intent(PennaFlameBaseActivity.this, ContactActivity.class);
                            break;
                    }

                    if (intent != null) {
                        if (position == 0) {
                            TaskStackBuilder.create(PennaFlameBaseActivity.this)
                                    // Add all of this activity's parents to the back stack
                                    .addNextIntent(intent)
                                    .startActivities();
                        } else {
                            Intent homeIntent = new Intent(PennaFlameBaseActivity.this, HomeActivity.class);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            TaskStackBuilder.create(PennaFlameBaseActivity.this)
                                    // Add all of this activity's parents to the back stack
                                    .addNextIntent(homeIntent)
                                    .addNextIntent(intent)
                                    .startActivities();
                        }
                    }
            }
        });

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
                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = li.inflate(R.layout.drawer_list_item, null);
            } else {
                row = convertView;
            }

            TextView tv = (TextView) row.findViewById(R.id.list_item_text);
            tv.setText(mSections[position]);
            return row;
        }

        @Override
        public int getCount() {
            return mSections.length;
        }
    }

}
