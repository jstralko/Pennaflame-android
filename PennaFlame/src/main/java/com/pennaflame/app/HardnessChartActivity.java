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

public class HardnessChartActivity extends PennaFlameBaseActivity {

    private HardnessDictionary mDictionary;

    //Fragments need access to these references
    private Spinner topSpinner;
    private Spinner rangeSpinner;
    private PFSpinnerAdapter currentAdapter;
    private PFSpinnerAdapter topSpinnerAdapter;
    private PFSpinnerAdapter rangeSpinnerAdapter;
    private WebView mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int titlesId = intent.getExtras().getInt(MainActivity.ROW_TITLE_ID);
        int keysId = intent.getExtras().getInt(MainActivity.KEYS_ID);

        mDictionary = new HardnessDictionary(this, titlesId, keysId);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.container, new HardnessChartFragment())
                    .add(R.id.container, new HardnessPickChartFragment())
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

    public class HardnessPickChartFragment extends Fragment {

        private Button showButton;

        public HardnessPickChartFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.picker_chart_main, container, false);
            mChart = (WebView)rootView.findViewById(R.id.chartWebView);
            //mChart.getSettings().setDisplayZoomControls(true);
            topSpinner = (Spinner)rootView.findViewById(R.id.topSpinner);
            topSpinnerAdapter = new PFSpinnerAdapter(topSpinner);
            topSpinner.setAdapter(topSpinnerAdapter);
            topSpinner.setOnItemSelectedListener(new PFItemSelectedListener());
            rangeSpinner = (Spinner)rootView.findViewById(R.id.rangeSpinner);
            rangeSpinnerAdapter = new PFSpinnerAdapter(rangeSpinner);
            rangeSpinner.setAdapter(rangeSpinnerAdapter);
            rangeSpinner.setOnItemSelectedListener(new PFItemSelectedListener());
            showButton = (Button)rootView.findViewById(R.id.showButton);
            showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new HardnessChartFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
            return rootView;
        }
    }

    private class PFItemSelectedListener implements Spinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
            if (view == topSpinner) {
                rangeSpinnerAdapter.notifyDataSetChanged();
                return;
            }

            //show chart
            int selectedRange = rangeSpinner.getSelectedItemPosition();
            StringBuilder html = new StringBuilder();
            html.append("<html><head></head><body style=\"background-color:#BDBBBB;\">" +
            "<table width=\"90%%\" border=\"1\" align=\"center\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CCCCC\">" +
            "<tbody>" +
            "<tr bgcolor=\"lightgrey\" align=\"center\">");
            for (String key : mDictionary.keySet()) {
                html.append(String.format("<td bgcolor=\"#FF0000\"><span style=\"font-weight:bold\">%s</span></td>", key));
            }
            html.append("</tr><tr bgcolor=\"white\">");

            for (String key : mDictionary.keySet()) {
                Log.d("PennaFlame", String.format("%s:%s", key,  mDictionary.get(key).get(selectedRange)));
                html.append(String.format("<td><div align=\"center\">%s</div></td>",
                        mDictionary.get(key).get(selectedRange)));
            }
            html.append("</tr></table></body></html>");

            mChart.setVisibility(View.VISIBLE);
            mChart.loadDataWithBaseURL(null, html.toString(), "text/html", "UTF-8", null);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
                /* no-op */
        }
    }

    private class PFSpinnerAdapter extends BaseAdapter {

        private Spinner owner;

        public PFSpinnerAdapter(Spinner owner) {
            this.owner = owner;
        }

        public int getCount() {
            if (owner == topSpinner) {
                return mDictionary.keySet().size();
            }

            int keyIndex = topSpinner.getSelectedItemPosition();
            Set<String> keys = mDictionary.keySet();
            String[] keysArray = keys.toArray(new String[] {});
            String key = keysArray[keyIndex];
            return mDictionary.get(key).size();
        }

        public long getItemId(int position) {
            return position;
        }

        public Object getItem(int position) {

            if (owner == topSpinner) {
                Set<String> keys = mDictionary.keySet();
                String[] keysArray = keys.toArray(new String[] {});
                return keysArray[position];
            }

            int keyIndex = topSpinner.getSelectedItemPosition();
            Set<String> keys = mDictionary.keySet();
            String[] keysArray = keys.toArray(new String[] {});
            String key = keysArray[keyIndex];
            return mDictionary.get(key).get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = HardnessChartActivity.this.getLayoutInflater();
            final View spinnerView = inflater.inflate(R.layout.spinner_layout, null);
            TextView tv = (TextView)spinnerView.findViewById(R.id.textViewForSpinner);

            CharSequence text;
            if (owner == topSpinner) {
                Set<String> keys = mDictionary.keySet();
                String[] keysArray = keys.toArray(new String[] {});
                text = keysArray[position];
            } else {
                int keyIndex = topSpinner.getSelectedItemPosition();
                Set<String> keys = mDictionary.keySet();
                String[] keysArray = keys.toArray(new String[] {});
                String key = keysArray[keyIndex];
                text = mDictionary.get(key).get(position);
            }
            tv.setText(text);

            return spinnerView;
        }
    }

}
