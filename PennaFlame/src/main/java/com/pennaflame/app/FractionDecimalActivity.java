package com.pennaflame.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FractionDecimalActivity extends PennaFlameBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fractionDecimal, R.layout.fd_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.fd_spinner_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                if (i == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FractionFragment())
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new DecimalFragment())
                            .commit();
                }
                return false;
            }
        });

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FractionFragment())
                    .commit();
        }
    }

    public static class DecimalFragment extends Fragment {
        public DecimalFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fd_decimal, container, false);
            return rootView;
        }
    }

    public static class FractionFragment extends Fragment {

        public FractionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fd_fraction, container, false);
            return rootView;
        }

        public static int gcdForNumber1(int m, int n) {
            while( m!= n) // execute loop until m == n
            {
                if( m > n)
                    m= m - n; // large - small , store the results in large variable<br>
                else
                    n= n - m;
            }
            return ( m); // m or n is GCD
        }

        public static int tenRaisedTopower(int decimalLength) {
            int answer = 10;
            while (decimalLength!= 1) {
                answer *= 10;
                decimalLength -- ;
            }
            return answer;
        }

        public static int[] floatToFraction(float decimalNumber) {
            String decimalString = String.format("%f", decimalNumber);
            String[] components = decimalString.split(".");
            int decimalLength = components[1].length();
            int n = tenRaisedTopower(decimalLength);
            int m = Integer.valueOf(components[1]);
            if (m != 0) {
                int gcd = gcdForNumber1(m, n);
                int numer = m/gcd;
                int deno = n/gcd;
                int fractionnumer = (Integer.valueOf(components[0]) * deno) + numer;
//                numeratorTextField.text = [NSString stringWithFormat:@"%d", fractionnumer];
//                denominatorTextField.text = [NSString stringWithFormat:@"%d", deno];
//                numeratorStepper.value = fractionnumer;
//                denominatorStepper.value = deno;
            } else {
//                numeratorTextField.text = components[0];
//                denominatorTextField.text = [NSString stringWithFormat:@"%d", 1];
//                numeratorStepper.value = (int)decimalNumber;
//                denominatorStepper.value = 1;
            }
            return null; //TODO: finish!!!!!!
        }

        //TODO: updateDecimal method from iOS project
    }
}
