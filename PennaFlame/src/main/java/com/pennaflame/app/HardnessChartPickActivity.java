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
            int titleId = intent.getExtras().getInt(HomeActivity.CHART_TITLE_ID);

            getSupportActionBar().setTitle(getString(titleId));
            mDictionary = new HardnessDictionary(this, titlesId, keysId);

            Fragment hpcf = HardnessPickChartFragment.newInstance(mDictionary);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, hpcf)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hardness_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
