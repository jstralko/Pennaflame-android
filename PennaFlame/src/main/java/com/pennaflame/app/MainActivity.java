package com.pennaflame.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            return 6;
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
            switch(position) {
                case 0:
                    button.setText("English/Metric Converter");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, MetricActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    button.setText("Fraction/Decimal Converter");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case 2:
                    button.setText("Hardness Case Depth");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case 3:
                    button.setText("MTI Statement");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case 4:
                    button.setText("Hardness Chart");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case 5:
                    button.setText("Contact Us");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

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
