package com.pennaflame.app;

/**
 * Created by stralko on 10/28/13.
 */

import android.app.Activity;
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

    public interface HardnessChartDataSourceInterface {
        public HardnessDictionary getHardnessDictionary();
        public String getChartTitle();
    }

    private static final String DISCLAIMER = "Note: This chart is a general guide.  " +
            "Hardness and case depth's may vary depending on the flame hardening technique " +
            "used and actual chemistry of the material.";

    private HardnessChartDataSourceInterface mDelegate;
    private WebView mChart;
    private WebView mMainChart;

    public HardnessChartFragment() {
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        HardnessChartFragment hcf = new HardnessChartFragment();
        hcf.setArguments(args);
        return hcf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_webview, container, false);
        mChart = (WebView) rootView.findViewById(R.id.wvChart);
        mChart.getSettings().setBuiltInZoomControls(false);
        mMainChart = (WebView)rootView.findViewById(R.id.wvChartMain);

        //make transparent
        mMainChart.setBackgroundColor(0x00000000);
        mChart.setBackgroundColor(0x00000000);

        String fontSize = "0.80em";
        int screenWith = DimensionUtils.getScreenWidth(getActivity());
        if (screenWith <= 480) {
            fontSize = "0.70em";
        }

        HardnessDictionary dictionary = mDelegate.getHardnessDictionary();
        StringBuilder html = new StringBuilder();
        html.append("<html><head></head><body style=\"background-color:transparent;\">" +
                "<table width=\"90%%\" border=\"1\" align=\"center\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CCCCC\">" +
                "<tbody>" +
                "<tr bgcolor=\"lightgrey\" align=\"center\">");
        for (String key : dictionary.keySet()) {
            html.append(String.format("<td bgcolor=\"#FF0000\"><span style=\"font-size:%s;font-weight:bold\">%s</span></td>", fontSize, key));
        }
        html.append("</tr></table></body></html>");

        mChart.loadData(html.toString(), "text/html", "UTF-8");

        html = new StringBuilder();
        html.append("<html><head></head><body style=\"background-color:transparent;\">" +
                "<table width=\"90%%\" border=\"1\" align=\"center\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CCCCC\">" +
                "<tbody>" +
                "<tr bgcolor=\"lightgrey\" align=\"center\">");
        for (String key : dictionary.keySet()) {
            html.append(String.format("<td bgcolor=\"white\"><span style=\"font-size:%s;font-weight:bold;color:white\">%s</span></td>", fontSize, key));
        }
        html.append("</tr>");

        int total = dictionary.get(dictionary.keySet().iterator().next()).size();
        for (int i = 0; i < total; i++) {
            html.append("<tr bgcolor=\"white\">");
            for (String key : dictionary.keySet()) {
                html.append(String.format("<td><div align=\"center\">%s</div></td>", dictionary.get(key).get(i)));
            }
        }
        html.append("</tr>");
        html.append("</table>");

        if (mDelegate.getChartTitle().equals(getString(R.string.title_activity_hardness_case_depth))) {
            //add the disclaimer
            html.append("<div style=\"text-align:center;margin-top:2%;margin-left:2%;margin-right:2%;font-size:0.8em;font:style:italic;\">");
            html.append(DISCLAIMER);
            html.append("</div>");
        }

        html.append("</body></html");
        mMainChart.loadData(html.toString(), "text/html", "UTF-8");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mChart.loadData(mHtml, "text/html", "UTF-8");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDelegate = (HardnessChartDataSourceInterface) activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + " must implement HardnessChartDataSourceInterface");
        }
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mDelegate = null;
    }
}
