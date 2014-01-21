package com.pennaflame.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class HardnessChartActivity extends ActionBarActivity {

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
            int titlesId = intent.getExtras().getInt(HomeActivity.ROW_TITLE_ID);
            int keysId = intent.getExtras().getInt(HomeActivity.KEYS_ID);
            mDictionary = new HardnessDictionary(this, titlesId, keysId);
            String html = generateChartHTML();

            mTitlesId = intent.getExtras().getInt(HomeActivity.ROW_TITLE_ID);
            mKeysId = intent.getExtras().getInt(HomeActivity.KEYS_ID);
            mTitleId = intent.getExtras().getInt(HomeActivity.CHART_TITLE_ID);
            Fragment f = HardnessChartFragment.newInstance(html);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f)
                    .commit();
        }
    }

    private String generateChartHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head></head><body style=\"background-color:#BDBBBB;\">" +
                "<table width=\"90%%\" border=\"1\" align=\"center\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CCCCC\">\n" +
                "<tbody>" +
                "<tr bgcolor=\"lightgrey\" align=\"center\">");
        for (String key : mDictionary.keySet()) {
            html.append(String.format("<td bgcolor=\"#FF0000\"><span style=\"font-weight:bold\">%s</span></td>", key));
        }
        html.append("</tr>");

        int total = mDictionary.get(mDictionary.keySet().iterator().next()).size();
        for (int i = 0; i < total; i++) {
            html.append("<tr bgcolor=\"white\">");
            for (String key : mDictionary.keySet()) {
                html.append(String.format("<td><div align=\"center\">%s</div></td>", mDictionary.get(key).get(i)));
            }
        }
        html.append("</tr>");
        html.append("</table></body></html");
        return html.toString();
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
