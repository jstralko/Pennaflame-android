package com.pennaflame.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class HomeActivity extends ActionBarActivity {

    public static final String ROW_TITLE_ID = "row_header_title_id";
    public static final String KEYS_ID = "keys_id";
    public static final String CHART_TITLE_ID = "chart_title_id";

    private String[] mSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSections = getResources().getStringArray(R.array.app_sections);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView)findViewById(R.id.home_layout);
        gridView.setAdapter(new HomeAdapter(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private class HomeAdapter extends BaseAdapter {
        private Context mContext;
        public HomeAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mSections.length - 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null){
                v = getLayoutInflater().from(mContext).inflate(R.layout.home_row, null);
            } else {
                v = convertView;
            }

            Button button = (Button)v.findViewById(R.id.homeButton);
            button.setText(mSections[position+1]);
            switch(position) {
                case 0:
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, MetricActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case 2:
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, HardnessChartPickActivity.class);
                            intent.putExtra(ROW_TITLE_ID, R.array.case_depth_row_header_titles);
                            intent.putExtra(KEYS_ID, R.array.case_depth_keys);
                            intent.putExtra(CHART_TITLE_ID, R.string.title_activity_hardness_case_depth);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, MTIStatementActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    break;
                case 4:
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, HardnessChartPickActivity.class);
                            intent.putExtra(ROW_TITLE_ID, R.array.row_header_titles);
                            intent.putExtra(KEYS_ID, R.array.keys);
                            intent.putExtra(CHART_TITLE_ID, R.string.title_activity_hardness_chart);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    break;
                case 5:
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, ContactActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    button.setText("UNKOWN");
            }
            return v;
        }
    }
}
