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
    private HardnessDictionary mDictionary;

    public HardnessChartFragment() {
    }

    public static Fragment newInstance(HardnessDictionary dictionary) {
        Bundle args = new Bundle();
        args.putSerializable("dictionary", dictionary);
        HardnessChartFragment hcf = new HardnessChartFragment();
        hcf.setArguments(args);
        return hcf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDictionary = (HardnessDictionary)getArguments().getSerializable("dictionary");
        View rootView = inflater.inflate(R.layout.fragment_main_webview, container, false);
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
