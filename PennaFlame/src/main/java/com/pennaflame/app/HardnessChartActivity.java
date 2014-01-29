package com.pennaflame.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HardnessChartActivity extends ActionBarActivity implements HardnessChartFragment.HardnessChartDataSourceInterface {

    private int mTitlesId;
    private int mKeysId;
    private int mTitleId;
    private HardnessDictionary mDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_chart);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mTitlesId = intent.getExtras().getInt(HomeActivity.ROW_TITLE_ID);
            mKeysId = intent.getExtras().getInt(HomeActivity.KEYS_ID);
            mTitleId = intent.getExtras().getInt(HomeActivity.CHART_TITLE_ID);

            mDictionary = new HardnessDictionary(this, mTitlesId, mKeysId);
            Fragment f = HardnessChartFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f)
                    .commit();
        } else {
            mTitlesId = savedInstanceState.getInt(HomeActivity.ROW_TITLE_ID);
            mKeysId = savedInstanceState.getInt(HomeActivity.KEYS_ID);
            mTitleId = savedInstanceState.getInt(HomeActivity.CHART_TITLE_ID);
            mDictionary = new HardnessDictionary(this, mTitlesId, mKeysId);
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(HomeActivity.ROW_TITLE_ID, mTitlesId);
        bundle.putInt(HomeActivity.KEYS_ID, mKeysId);
        bundle.putInt(HomeActivity.CHART_TITLE_ID, mTitleId);
    }

    public HardnessDictionary getHardnessDictionary() {
        return mDictionary;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            /*
             * This way is probably overkill, I'm learning how parent/child activity
             * framework works.
             * Since back and up will _always_ go to the same activity within the same
             * app, a finish() call would have worked with passing upIntent.
             * However, I want to fully understand how this stuff works, since it
             * kind of new (for me atleast).
             *
             * If this give you problems, feel free to remove it in favor of
             * finish()
             */
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(HomeActivity.ROW_TITLE_ID, mTitlesId);
                upIntent.putExtra(HomeActivity.KEYS_ID, mKeysId);
                upIntent.putExtra(HomeActivity.CHART_TITLE_ID, mTitleId);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
