package com.pennaflame.app;

/**
 * Created by stralko on 10/28/13.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A placeholder fragment containing a simple view.
 */
public class HardnessChartFragment extends Fragment {

    private WebView mChart;
    private String mHtml;

    public HardnessChartFragment() {
    }

    public static Fragment newInstance(String html) {
        Bundle args = new Bundle();
        args.putString("html", html);
        HardnessChartFragment hcf = new HardnessChartFragment();
        hcf.setArguments(args);
        return hcf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHtml = getArguments().getString("html");
        View rootView = inflater.inflate(R.layout.fragment_main_webview, container, false);
        mChart = (WebView) rootView.findViewById(R.id.wvChart);
        mChart.getSettings().setBuiltInZoomControls(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            mChart.getSettings().setDisplayZoomControls(false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mChart.loadData(mHtml, "text/html", "UTF-8");
    }
}
