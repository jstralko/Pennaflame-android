package com.pennaflame.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by stralko on 10/5/13.
 */
public class MetricFragment extends Fragment {

    public static final int ENGLISH_TYPE = 0;
    public static final int METRIC_TYPE = 1;

    public static MetricFragment newInstance(int type) {
        MetricFragment metricFragment = new MetricFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        metricFragment.setArguments(args);
        return metricFragment;
    }

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
