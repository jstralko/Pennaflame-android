package com.pennaflame.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by stralko on 10/28/13.
 */
public class HardnessPickChartFragment extends Fragment implements Spinner.OnItemSelectedListener {

    private Button showButton;
    //Fragments need access to these references
    private Spinner topSpinner;
    private Spinner rangeSpinner;
    private PFSpinnerAdapter topSpinnerAdapter;
    private PFSpinnerAdapter rangeSpinnerAdapter;
    private WebView mChart;
    private HardnessDictionary mDictionary;

    public HardnessPickChartFragment() {

    }

    public static Fragment newInstance(HardnessDictionary dictionary) {
        Bundle args = new Bundle();
        args.putSerializable("dictionary", dictionary);
        HardnessPickChartFragment hpcf = new HardnessPickChartFragment();
        hpcf.setArguments(args);
        return hpcf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: add error handling more gracefully!
        mDictionary = (HardnessDictionary)getArguments().getSerializable("dictionary");
        View rootView = inflater.inflate(R.layout.fragment_picker_chart_main, container, false);
        mChart = (WebView)rootView.findViewById(R.id.chartWebView);
        //mChart.getSettings().setDisplayZoomControls(true);
        topSpinner = (Spinner)rootView.findViewById(R.id.topSpinner);
        topSpinnerAdapter = new PFSpinnerAdapter(topSpinner);
        topSpinner.setAdapter(topSpinnerAdapter);
        topSpinner.setOnItemSelectedListener(this);
        rangeSpinner = (Spinner)rootView.findViewById(R.id.rangeSpinner);
        rangeSpinnerAdapter = new PFSpinnerAdapter(rangeSpinner);
        rangeSpinner.setAdapter(rangeSpinnerAdapter);
        rangeSpinner.setOnItemSelectedListener(this);
        showButton = (Button)rootView.findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = HardnessChartFragment.newInstance(mDictionary);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, f)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return rootView;
    }

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
            Log.d("PennaFlame", String.format("%s:%s", key, mDictionary.get(key).get(selectedRange)));
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
            final LayoutInflater inflater = getActivity().getLayoutInflater();
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