package com.pennaflame.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class HardnessChartActivity extends PennaFlameBaseActivity {

    private HardnessDictionary mDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        mDictionary = new HardnessDictionary(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HardnessChartFragment())
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public class HardnessChartFragment extends Fragment {

        private WebView mChart;

        public HardnessChartFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.hardness_chart_main, container, false);
            mChart = (WebView)rootView.findViewById(R.id.wvChart);
            return rootView;
        }

        @Override
        public void onActivityCreated (Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

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

            mChart.loadData(html.toString(), "text/html", "UTF-8");
        }
    }
}
