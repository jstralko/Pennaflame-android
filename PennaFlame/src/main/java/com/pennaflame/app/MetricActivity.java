package com.pennaflame.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MetricActivity extends PennaFlameBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MetricFragment())
                    .commit();
        }
    }

    public static MetricFragment newInstance(int type) {
        MetricFragment metricFragment = new MetricFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        metricFragment.setArguments(args);
        return metricFragment;
    }

    public static class MetricFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_metric, null);
            return view;
        }
    }
}
