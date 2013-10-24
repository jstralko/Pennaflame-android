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

public class MetricActivity extends PennaFlameBaseActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    private MetricFragment englishFragment;
    private MetricFragment metricFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                Fragment fragment;
                if (tab.getText().equals("English")) {
                    if (englishFragment == null) {
                        englishFragment = MetricActivity.newInstance(MetricFragment.ENGLISH_TYPE);
                    }
                    fragment = englishFragment;
                } else {
                    if (metricFragment == null) {
                        metricFragment = MetricActivity.newInstance(MetricFragment.METRIC_TYPE);
                    }
                    fragment = metricFragment;
                }
                //switch to the new fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.metric, menu);
        return true;
    }

    public static MetricFragment newInstance(int type) {
        MetricFragment metricFragment = new MetricFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        metricFragment.setArguments(args);
        return metricFragment;
    }

    public static class MetricFragment extends Fragment {

        public static final int ENGLISH_TYPE = 0;
        public static final int METRIC_TYPE = 1;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.english_layout, null);
            Bundle extras = getArguments();
            int type = extras.getInt("type");
            if (type == METRIC_TYPE) {
                TextView tv = (TextView)view.findViewById(R.id.textView);
                tv.setText("Metric Fragment");
            }
            return view;
        }
    }
}
