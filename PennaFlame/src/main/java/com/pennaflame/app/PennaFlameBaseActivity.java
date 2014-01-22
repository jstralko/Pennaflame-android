package com.pennaflame.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by stralko on 10/23/13.
 */
public class PennaFlameBaseActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    private String[] mSections;

    public static final int HOME_INDEX          = 0;
    public static final int METRIC_INDEX        = 1;
    public static final int FRACTION_INDEX      = 2;
    public static final int CASE_DEPTH_INDEX    = 3;
    public static final int HARDNESS_INDEX      = 4;
    public static final int MTI_INDEX           = 5;
    public static final int CONTACT_INDEX       = 6;

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
                    case HOME_INDEX:
                        intent = new Intent(PennaFlameBaseActivity.this, HomeActivity.class);
                        break;
                    case METRIC_INDEX:
                        intent = new Intent(PennaFlameBaseActivity.this, MetricActivity.class);
                        break;
                    case FRACTION_INDEX:
                        intent = new Intent(PennaFlameBaseActivity.this, FractionDecimalActivity.class);
                        break;
                    case CASE_DEPTH_INDEX:
                        intent = new Intent(PennaFlameBaseActivity.this, HardnessChartPickActivity.class);
                        intent.putExtra(HomeActivity.ROW_TITLE_ID, R.array.case_depth_row_header_titles);
                        intent.putExtra(HomeActivity.KEYS_ID, R.array.case_depth_keys);
                        intent.putExtra(HomeActivity.CHART_TITLE_ID, R.string.title_activity_hardness_case_depth);
                        break;
                    case HARDNESS_INDEX:
                        intent = new Intent(PennaFlameBaseActivity.this, HardnessChartPickActivity.class);
                        intent.putExtra(HomeActivity.ROW_TITLE_ID, R.array.row_header_titles);
                        intent.putExtra(HomeActivity.KEYS_ID, R.array.keys);
                        intent.putExtra(HomeActivity.CHART_TITLE_ID, R.string.title_activity_hardness_chart);
                        break;
                    case MTI_INDEX:
                        intent = new Intent(PennaFlameBaseActivity.this, MTIStatementActivity.class);
                        break;
                    case CONTACT_INDEX:
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

    protected boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerList);
    }

    protected void closeDrawer() {
        mDrawerLayout.closeDrawers();
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

            ImageView imageView = (ImageView)row.findViewById(R.id.iv_list_item);

            TextView tv = (TextView) row.findViewById(R.id.list_item_text);
            tv.setText(mSections[position]);

            Bitmap image = null;
            switch (position) {
                case HOME_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.pfi_app_icon);
                    break;
                case METRIC_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.english_metric_converter);
                    break;
                case FRACTION_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.fraction_decimal_converter);
                    break;
                case CASE_DEPTH_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.case_depth);
                    break;
                case HARDNESS_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.hardness_chart);
                    break;
                case MTI_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.mti_statement);
                    break;
                case CONTACT_INDEX:
                    image = BitmapFactory.decodeResource(PennaFlameBaseActivity.this.getResources(), R.drawable.contact);
                    break;
                default:
            }

            if (image != null) {
                imageView.setImageBitmap(image);
            }
            return row;
        }

        @Override
        public int getCount() {
            return mSections.length;
        }
    }

}
