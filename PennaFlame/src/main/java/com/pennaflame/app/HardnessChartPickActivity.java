package com.pennaflame.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Set;

public class HardnessChartPickActivity extends PennaFlameBaseActivity {

    private HardnessDictionary mDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (savedInstanceState == null) {
            int titlesId = intent.getExtras().getInt(HomeActivity.ROW_TITLE_ID);
            int keysId = intent.getExtras().getInt(HomeActivity.KEYS_ID);
            String title = intent.getExtras().getString(HomeActivity.CHART_TITLE_ID);

            getSupportActionBar().setTitle(title);
            mDictionary = new HardnessDictionary(this, titlesId, keysId);

            Fragment hpcf = HardnessPickChartFragment.newInstance(mDictionary);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, hpcf)
                    .commit();
        }
    }
}
